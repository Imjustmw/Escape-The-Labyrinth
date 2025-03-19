import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

public abstract class InteractiveObject {
    protected int x, y;
    protected boolean isActive;

    protected HashMap<String, Animation> animations;
    protected Animation currentAnimation = null;
    protected String currentState = "";

    protected SoundManager soundManager;

    public InteractiveObject(int x, int y) {
        soundManager = SoundManager.getInstance();
        this.x = x;
        this.y = y;
        this.isActive = true;
        animations = new HashMap<String, Animation>();
    }

    public Animation getCurrentAnimation() { return this.currentAnimation; }

    public void draw(Graphics2D g2) {
        if (isActive && currentAnimation != null) {
            g2.drawImage(currentAnimation.getImage(), x, y, GameWindow.tileSize, GameWindow.tileSize, null);
        }
            
    }

    public void update() {
        if (currentAnimation != null && currentAnimation.isStillActive()) 
            currentAnimation.update();   
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameWindow.tileSize, GameWindow.tileSize);
    }

    public abstract void interact(Player player);

    protected abstract void addAnimations();

    protected void setState(String state) {
        if (currentState.equals(state)) return;
        if (currentAnimation != null) currentAnimation.stop();
        
        currentState = state;
        currentAnimation = animations.get(currentState);
        currentAnimation.start();
    }
}
