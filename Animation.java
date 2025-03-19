import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
    private ArrayList<AnimFrame> frames;
    private int currentFrameIndex;
    private long animTime;
    private long totalDuration;
    private long startTime;
    private boolean isActive;
    private boolean isPaused;
    private boolean loop;

    public Animation(boolean loop) {
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
        this.loop = loop;
        isActive = false;
    }

    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    public synchronized void addFramesFromSpriteSheet(BufferedImage spriteSheet, int rows, int cols, long frameDuration) {
        int frameWidth = spriteSheet.getWidth() / cols;
        int frameHeight = spriteSheet.getHeight() / rows;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                BufferedImage frame = spriteSheet.getSubimage(x * frameWidth, y * frameHeight, frameWidth, frameHeight);
                addFrame(frame, frameDuration);
            }
        }
    }

    public synchronized void start() {
        isActive = true;
        isPaused = false;
        startTime = System.currentTimeMillis();
        animTime = 0;
        currentFrameIndex = 0;
    }

    public synchronized void stop() {
        isActive = false;
        isPaused = false;
    }

    public synchronized void pause(boolean isPaused) {
        if (isActive)
            this.isPaused = isPaused;
        
        if (!isPaused)
            startTime = System.currentTimeMillis();
    }

    public synchronized void update() { // update the animation
        if (!isActive || isPaused)
            return;

        long now = System.currentTimeMillis();
        long elapsedTime = now - startTime;
        startTime = now;

        if (frames.size() > 1) {
            animTime += elapsedTime;
            if (animTime >= totalDuration) {
                if (loop) {
                    animTime = animTime % totalDuration;
                    currentFrameIndex = 0;
                } else {
                    isActive = false;
                    return;
                }
            }

            while (animTime > getFrame(currentFrameIndex).getEndTime()) {
                currentFrameIndex++;
            }
        }
    }

    public boolean isStillActive() { return isActive; }

    public synchronized Image getImage() { // return the current frame image
        if (frames.size() == 0)
            return null;
        else
            return getFrame(currentFrameIndex).getImage();
    }

    private AnimFrame getFrame(int i) { // return the frame at index i in the collection
        return frames.get(i);
    }

}
