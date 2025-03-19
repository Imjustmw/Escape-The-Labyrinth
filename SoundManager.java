import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager { // a Singleton Class
    private HashMap<String, Clip> sounds;
    private HashMap<String, Integer> pausedClips; // store paused clip positions
    private HashMap<String, Boolean> pausedLoops; // store whether clip was looping or not

    private static SoundManager instance = null;

    private SoundManager() {
        sounds = new HashMap<>();
        pausedClips = new HashMap<>();
        pausedLoops = new HashMap<>();

        // load sounds
        sounds.put("background", loadSound("sounds/background.wav"));
        sounds.put("key", loadSound("sounds/key_pickup.wav"));
        sounds.put("move_1", loadSound("sounds/move_1.wav"));
        sounds.put("move_2", loadSound("sounds/move_2.wav"));
        sounds.put("move_3", loadSound("sounds/move_3.wav"));
        sounds.put("slash_1", loadSound("sounds/slash_1.wav"));
        sounds.put("death_1", loadSound("sounds/death_1.wav"));
        sounds.put("teleport", loadSound("sounds/teleport.wav"));
        sounds.put("intro", loadSound("sounds/lets_do_this.wav"));
    }

    public Clip getSound(String name) {  return sounds.get(name); }

    public static SoundManager getInstance() { // class method to retrieve instance of Singleton
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }

    public Clip loadSound(String fileName) { // gets clip from the specified file
        AudioInputStream audioIn;
        Clip clip = null;
        try {
            File file = new File(fileName);
            audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e) {
            System.err.println("Error loading sound: " + fileName + '\n' + e.getMessage());
        }
        return clip;
    }

    public void playSound(String name, boolean looping) { // play the sound
        Clip sound = getSound(name);
        if (sound != null) {
            if (sound.isRunning())
                sound.stop();
            sound.setFramePosition(0);
            if (looping) {
                sound.loop(Clip.LOOP_CONTINUOUSLY);
                pausedLoops.put(name, true);
            }
            else {
                sound.start();
                pausedLoops.put(name, false);
            }   
        }
    }


    public void stopSound(String name) { // stop the sound
        Clip sound = getSound(name);
        if (sound != null) {
            sound.stop();
            sound.flush();
        } 
    }

    public void stopAllSounds() {
        for (Clip sound : sounds.values()) {
            if (sound.isRunning()) {
                sound.stop();
                sound.flush();
            }
        }
        pausedClips.clear();
        pausedLoops.clear(); 
    }

    public void pauseAllSounds() {
        for (String name : sounds.keySet()) {
            Clip sound = sounds.get(name);
            if (sound != null && sound.isRunning()) {
                pausedClips.put(name, sound.getFramePosition()); // Save position
                sound.stop();
            }
        }
       
    }

    public void resumeAllSounds() {
        for (String name : pausedClips.keySet()) {
            Clip sound = sounds.get(name);
            if (sound != null) {
                sound.setFramePosition(pausedClips.get(name)); // Restore position
                if (pausedLoops.getOrDefault(name, false))
                    sound.loop(Clip.LOOP_CONTINUOUSLY);
                else
                    sound.start();
            }
        }
        pausedClips.clear();
    }

    public void setVolume(String name, float volume) { // set the volume of the sound
        Clip sound = getSound(name);
        if (sound != null) {
            FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);

            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();

            gainControl.setValue(gain);
        }
    }

}
