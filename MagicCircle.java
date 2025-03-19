import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MagicCircle extends InteractiveObject {
    public MagicCircle(int x,int y) {
        super(x,y);
        addAnimations();
        setState("idle");
    }

    public void addAnimations() {
        animations.put("idle", AnimationManager.createAnimation("images/objects/magic_circle3.png", true, 4, 4, 300));
    }

    public void interact(Player player) {
        if (isActive) {
            if (player.hasPermit()) {
                player.setState("teleport",false);
                player.finish(true);

                // Play Sound Effect
                soundManager.playSound("teleport", false);

                // Go to next Floor/Level
                Timer timer = new Timer(1500, new ActionListener() { // Delay by 1.5 seconds to allow animation to finish
                    public void actionPerformed(ActionEvent e) {
                        player.getGamePanel().nextLevel();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}
