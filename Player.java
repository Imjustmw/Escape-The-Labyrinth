import java.awt.Graphics2D;

public class Player extends Entity {
    private int moveSFXIndex;
    private boolean permit; // Permit allows you to move to next level
    private boolean finished; // Game knows when player has finished level
    private boolean attacking; // Player is attaclmg sp cant do anything else in this state

    public Player(GamePanel panel, int x, int y, int speed, Map map, String facing) {
        super(panel, x, y, GameWindow.tileSize, GameWindow.tileSize, speed, map, facing);
        this.moveSFXIndex = 0;
        this.permit = false;
        this.finished = false;
        this.attacking = false;

        // Load player Animation    
        addAnimations();
        setState("idle",true);
    }

    public boolean hasPermit() { return this.permit; }
    public void setPermit(boolean permit) { this.permit = permit; }
    public void finish(boolean finished) { this.finished = finished; }

    public void draw(Graphics2D g2) { super.draw(g2); }

    public void update() { 
        super.update();
        if (isMoving) {
            smoothMove();
        } else if (!currentAnimation.isStillActive() && !finished && alive) {
           setState("idle",true);
           if (attacking) attacking = false;
        }
    }

    public void move(int direction) {
        if (isMoving || finished || attacking) return; // Ignore input until finished moving to target position


        int newX = x, newY = y;
        
        if (direction == 0) { // Move up
            newY = Math.max(0, y - tileSize);
        } else if (direction == 1) {  // Move down
            newY = Math.min(dimensions.height - height, y + tileSize);
        } else if (direction == 2) { // Move Left
            newX = Math.max(0,  x - tileSize);
            facing = "left";
            setState("idle",true);
        } else if (direction == 3) { // move Right
            newX = Math.min(dimensions.width - width, x + tileSize);
            facing = "right";
            setState("idle",true);
        }

        // Check for Enemy
        Enemy enemy = (Enemy) map.getEntityAt(newX, newY);
        if (enemy != null) {
           // Kill Enemy
           setState("attack",true);
           attacking = true;
           soundManager.playSound("slash_1", false);
           enemy.kill();
           map.playerMoved();
           return;
        }

        // Check If Tile is Moveable
        if (canMove(newX, newY)) {
            map.playerMoved(); // Update Moves Remaining
            if (!alive) return;

            targetX = newX;
            targetY = newY;
            isMoving = true;
            setState("move",true);

            // Add dash effect
            Animation dash_anim = (facing.equals("right"))
            ? AnimationManager.createAnimation("images/effects/smoke_dash.png", false, 1, 8, 50)
            : AnimationManager.createFlippedAnimation("images/effects/smoke_dash.png", false, 1, 8, 50);
            EffectsManager.addEffect(x, y, tileSize, tileSize, dash_anim, false);

            // Sound effect
            moveSFXIndex = (moveSFXIndex % 3) + 1;
            soundManager.playSound("move_" + moveSFXIndex, false);
        }
    }

    private void smoothMove() {
        if (x < targetX) x += Math.min(speed, targetX - x);
        if (x > targetX) x -=  Math.min(speed, x - targetX);
        if (y < targetY) y +=  Math.min(speed, targetY - y);
        if (y > targetY) y -=  Math.min(speed, y - targetY);

        if (x == targetX && y == targetY) { // Finished moving
            // Check for interactive objects
            InteractiveObject object = map.getObjectAt(targetX, targetY);
            if (object != null) object.interact(this);
            
            // Complete movement
            isMoving = false;
        }
    }

    public void kill() {
        super.kill();
        setState("death", true);
    }

    public void addAnimations() {
        animations.put("idle_left", AnimationManager.createAnimation("images/player/cat.png", true, 1, 6, 100));
        animations.put("move_left", AnimationManager.createAnimation("images/player/cat_dash.png", false, 1, 2, 85));
        animations.put("attack_left", AnimationManager.createAnimation("images/player/cat_attack.png", false, 1, 6, 100));
        animations.put("death_left", AnimationManager.createAnimation("images/player/cat_death.png", false, 1, 5, 80));

        animations.put("idle_right", AnimationManager.createFlippedAnimation("images/player/cat.png", true, 1, 6, 100));
        animations.put("move_right", AnimationManager.createFlippedAnimation("images/player/cat_dash.png", false, 1, 2, 85));
        animations.put("attack_right", AnimationManager.createFlippedAnimation("images/player/cat_attack.png", false, 1, 6, 100));
        animations.put("death_right", AnimationManager.createFlippedAnimation("images/player/cat_death.png", false, 1, 5, 80));

        animations.put("teleport", AnimationManager.createAnimation("images/player/cat_teleport.png", false, 4, 4, 100));
    }
}
