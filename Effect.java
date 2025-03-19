import java.awt.Graphics2D;

public class Effect {
    private Animation animation;
    private int x, y, width, height;
    private boolean abovePlayer;

    public Effect(int x, int y, int width, int height, Animation animation, boolean abovePlayer) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.animation = animation;
        this.abovePlayer = abovePlayer;

        animation.start();
    }

    public void draw(Graphics2D g2) {
        if (animation != null)
            g2.drawImage(animation.getImage(), x, y, width, height, null);
    }

    public void update() {
        if (animation != null && animation.isStillActive())
            animation.update();
    }

    public boolean isActive() {
        return animation != null && animation.isStillActive();
    }

    public boolean isAbovePlayer() { return this.abovePlayer; }

}
