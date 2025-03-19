import java.awt.image.BufferedImage;

public class Floor3 extends Map{
    public Floor3(GamePanel panel, HUDPanel hud) {
        super(panel, hud, 32);
        //super.map = loadMap();
        //super.tileIds = loadTiles();
        BufferedImage spriteSheet = ImageManager.loadBufferedImage("images/maps/floor3/map.png");
        super.loadMapFromSpriteSheet(spriteSheet);
        super.objectMap = loadObjects();
    }

    // All Maps are 14x10
    protected int[][] loadMap() {
        // Load or generate the map specific to this floor
        return new int[][] {
        //   0  1  2  3  4  5  6  7  8  9 10 11 12 13
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 0
            {0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0}, // 1
            {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0}, // 2
            {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0}, // 3
            {0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0}, // 4
            {0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0}, // 5
            {0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0}, // 6
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, // 7
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, // 8
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 9
        };
    }

    protected Tile[] loadTiles() {
        // Load or generate the tiles specific to this floor
        return new Tile[] {
            new Tile(null, true), // 0 (null tile)
        };
    }

    protected InteractiveObject[][] loadObjects() {
        // Load or generate the objects specific to this floor
        InteractiveObject[][] grid = new InteractiveObject[10][14]; // Initial grid of null objects

        // Add Interactive Objects to the grid (map)
        grid[3][6] = new Key(6*tileSize, 3*tileSize);
        grid[9][13] = new MagicCircle(13*tileSize, 9*tileSize);
        return grid;
    }

    public void createEntities() {
        Entity entity = new Enemy(panel, 0 * tileSize, 4 * tileSize, 4, this, "right");
        addEntity(entity);

        entity = new Enemy(panel, 2 * tileSize, 7 * tileSize, 4, this, "right");
        addEntity(entity);

        entity = new Enemy(panel, 3 * tileSize, 5 * tileSize, 4, this, "left");
        addEntity(entity);

        entity = new Enemy(panel, 9 * tileSize, 2 * tileSize, 4, this, "left");
        addEntity(entity);

        entity = new Enemy(panel, 9 * tileSize, 5 * tileSize, 4, this, "right");
        addEntity(entity);

        entity = new Enemy(panel, 13 * tileSize, 4 * tileSize, 4, this, "left");
        addEntity(entity);

        // Create Player (Created last so that player can be drawn on top of all other entities)
        entity = new Player(panel, 1 * tileSize, 8 * tileSize, 4, this, "right");
        addEntity(entity);
        this.player = (Player) entity; // Add player to control
    }
}