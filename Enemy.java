import java.awt.Graphics2D;

public class Enemy extends Entity {
    public Enemy(GamePanel panel, int x, int y, int speed, Map map, String facing) {
        super(panel, x, y, GameWindow.tileSize, GameWindow.tileSize, speed, map, facing);

        // Load player Animation
        addAnimations();
        setState("idle", true);
    }

    public void draw(Graphics2D g2) { super.draw(g2); }

    public void update() {
        super.update();
        if (!alive) return;

        if (isMoving) {
            smoothMove();
        } else if (currentAnimation != null && !currentAnimation.isStillActive()) {
           setState("idle", true);
        }
    }

    public void move(int direction) {
        if (isMoving) return; // Ignore input until finished moving to target position

        int newX = x, newY = y;

        if (direction == 0) // Move up
            newY = Math.max(0, y - tileSize);
        else if (direction == 1)  // Move down
            newY = Math.min(dimensions.height - height, y + tileSize);
        else if (direction == 2) { // Move Left
            newX = Math.max(0,  x - tileSize);
            facing = "left";
            //setState("idle");
        }
        else if (direction == 3) { // move Right
            newX = Math.min(dimensions.width - width, x + tileSize);
            facing = "right";
            //setState("idle");
        }

        if (canMove(newX, newY)) {
            targetX = newX;
            targetY = newY;
            isMoving = true;
            //setState("move");
        }
    }
    
    private void smoothMove() {
        if (x < targetX) x += Math.min(speed, targetX - x);
        if (x > targetX) x -=  Math.min(speed, x - targetX);
        if (y < targetY) y +=  Math.min(speed, targetY - y);
        if (y > targetY) y -=  Math.min(speed, y - targetY);

        if (x == targetX && y == targetY) { // Finished moving
            // Complete movement
            isMoving = false;
            //setState("idle");
        }
    }

    public void kill() {
        super.kill();
        setState("death", true);
    }

    public void addAnimations() {
        animations.put("idle_left", AnimationManager.createAnimation("images/enemies/lantern.png", true, 1, 5, 100));
        animations.put("death_left", AnimationManager.createAnimation("images/enemies/lantern_death.png", false, 1, 6, 100));

        animations.put("idle_right", AnimationManager.createFlippedAnimation("images/enemies/lantern.png", true, 1, 5, 100));
        animations.put("death_right", AnimationManager.createFlippedAnimation("images/enemies/lantern_death.png", false, 1, 6, 100));
    }
}
