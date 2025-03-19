import java.awt.Graphics2D;
import java.awt.Dimension;

import java.util.HashMap;

public abstract class Entity {
    protected GamePanel panel;
    protected Dimension dimensions;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int speed;
    protected boolean alive;

    protected boolean isMoving;
    protected int targetX;
    protected int targetY;
    protected String facing;

    protected final int tileSize = GameWindow.tileSize;
    protected final int offset = (int) (tileSize * 0.25); // Offset to give illusion of depth
    protected Map map;

    protected HashMap<String, Animation> animations;
    protected Animation currentAnimation = null;
    protected String currentState = "";

    protected SoundManager soundManager;

    public Entity(GamePanel panel, int x, int y, int width, int height, int speed, Map map, String facing) {
        this.panel = panel;
        this.dimensions = panel.getSize();
        this.alive = true;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.targetX = x;
        this.targetY = y;
        this.facing = facing;
        this.isMoving = false;
        this.map = map;

        animations = new HashMap<String, Animation>();
        soundManager = SoundManager.getInstance();
    }

    public GamePanel getGamePanel() { return this.panel; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public boolean isAlive() { return this.alive; }
    public Animation getCurrentAnimation() { return this.currentAnimation; }
    
    public void draw(Graphics2D g2) {
        if (currentAnimation != null)
            g2.drawImage(currentAnimation.getImage(), x, y-offset, width, height, null);
    }

    public void update() {
        if (currentAnimation != null && currentAnimation.isStillActive())
            currentAnimation.update();
    }

    public abstract void addAnimations();

    public void setState(String state, boolean directionBased) {
        String newState = directionBased? (state + "_" + facing) : state;
        if (currentState.equals(newState)) return;
        if (currentAnimation != null) currentAnimation.stop();
        
        currentState = newState;
        currentAnimation = animations.get(currentState);
        if (currentAnimation != null) currentAnimation.start();
    }

    public boolean canMove(int newX, int newY) {
        int tileX = newX / tileSize;
        int tileY = newY / tileSize;
        Tile tile = map.getTile(tileX, tileY);

        // Check if tile is collidable
        return !tile.isCollidable();
    }

    public void kill() {
        alive = false;
        // Hit FX
        Animation hitfx = AnimationManager.createAnimation("images/effects/hitfx.png", false, 1, 6, 50);
        EffectsManager.addEffect(x, y, tileSize, tileSize, hitfx, true);

        // Sound FX
        soundManager.playSound("death_1", false);
    }
}
