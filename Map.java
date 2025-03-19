import java.util.ArrayList;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public abstract class Map {
    protected GamePanel panel;
    protected HUDPanel hud;
    protected int[][] map;
    protected InteractiveObject[][] objectMap;
    protected Tile[] tileIds;
    protected int tileSize;

    protected ArrayList<Entity> entities; // Store all Entities in Map instead of GamePanel (Collective Rendering and Modular Collision Detection)
    protected Player player;
    protected int maxMoves;
    protected int movesRemaining;

    public Map(GamePanel panel, HUDPanel hud, int maxMoves) {
        this.panel = panel;
        this.hud = hud;
        this.tileSize = GameWindow.tileSize;
        this.entities = new ArrayList<Entity>();
        this.player = null;
        this.maxMoves = maxMoves;
        this.movesRemaining = maxMoves;
    }

    public Player getPlayer() { return this.player; }
    public HUDPanel getHudPanel() { return this.hud; }
    public int getMovesRemaining() { return this.movesRemaining; }

    public void playerMoved() {
        if (movesRemaining > 0) {
            movesRemaining--;
            hud.updateMoves(movesRemaining);
        } else {
            player.kill(); // Kill Player

            // Restart Level
            Timer timer = new Timer(1500, new ActionListener() { // Delay by 1.5 seconds to allow animation to finish
                public void actionPerformed(ActionEvent e) {
                    resetMap();
                    panel.loadLevel();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    public void updateHud(int level) {
        hud.updateLevel(level);
        hud.updateMoves(movesRemaining);
    }
    
    protected abstract int[][] loadMap();
    protected abstract Tile[] loadTiles();
    protected abstract InteractiveObject[][] loadObjects();

    protected void resetMap() {
        this.movesRemaining = this.maxMoves;
        this.objectMap = loadObjects();
        removeEntities();
    }

    protected void loadMapFromSpriteSheet(BufferedImage spriteSheet) { // Load the map given a custom spritesheet
        int cols = spriteSheet.getWidth() / tileSize;
        int rows = spriteSheet.getHeight() / tileSize;

        this.map = new int[10][14];

        ArrayList<Tile> tileList = new ArrayList<>();
        tileList.add(new Tile(null, true)); // Default "null tile" at index 0

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                BufferedImage tileImage = spriteSheet.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize);
                if (!ImageManager.isBlankImage(tileImage)) {
                    tileList.add(new Tile(tileImage, false));
                    this.map[y][x] = (tileList.size()-1);
                } else {
                    this.map[y][x] = 0;
                }
            }
        }

        this.tileIds = tileList.toArray(new Tile[0]);
    }

    public final void draw(Graphics2D g2) {
        // Draw Map
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                // Draw Map Tiles
                int tileType = map[row][col];
                if (tileType == 0) continue;
                Image tileImage = tileIds[tileType].getImage();
                g2.drawImage(tileImage, col * tileSize, row * tileSize, tileSize, tileSize, null);

                // Draw Interactive Objects
                InteractiveObject object = objectMap[row][col];
                if (object != null)
                    object.draw(g2);
            }
        }
    }

    public final void drawEntities(Graphics2D g2) { // Separate for more control over z-index
         // Draw Entities
         for (Entity entity: entities) {
            entity.draw(g2);
        }
    }

    public final void update() {
        // Update Interactive Objects
        for (int row = 0; row < objectMap.length; row++) {
            for (int col = 0; col < objectMap[row].length; col++) {
                if (objectMap[row][col] != null)
                    objectMap[row][col].update();
            }
        }

        // Update Entities
        for (Entity entity: entities) {
            entity.update();
        }
    }

    public Tile getTile(int col, int row) {
        if (map[row][col] == -1) return null;
        return tileIds[map[row][col]];
    }

    public final boolean isCollidable(int x, int y) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        return tileIds[map[tileY][tileX]].isCollidable();
    }

    // Checking for Interactive Objects
    public InteractiveObject getObjectAt(int x, int y) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        return objectMap[tileY][tileX]; // most efficient way to get object
    }

    /* Entity Handling */
    public void addEntity(Entity entity) { entities.add(entity); }
    public void removeEntity(Entity entity) { entities.remove(entity); }
    public abstract void createEntities();
    public void removeEntities() { entities.clear(); }

    public void pause(boolean isPaused) {
        // Pause Interactive Objects
        for (int row = 0; row < objectMap.length; row++) {
            for (int col = 0; col < objectMap[row].length; col++) {
                if (objectMap[row][col] != null)
                    objectMap[row][col].getCurrentAnimation().pause(isPaused);
            }
        }

        // Pause Entities
        for (Entity entity: entities) {
            entity.getCurrentAnimation().pause(isPaused);
        }
    }

    public Entity getEntityAt(int x, int y) {
        for (Entity entity : entities) {
            if (entity != player && entity.isAlive() && entity.getX() == x && entity.getY() == y) {
                return entity;
            }
        }
        return null;
    }
}
