import java.awt.image.BufferedImage;

public class Floor2 extends Map{
    public Floor2(GamePanel panel, HUDPanel hud) {
        super(panel, hud, 36);
        //super.map = loadMap();
        //super.tileIds = loadTiles();
        BufferedImage spriteSheet = ImageManager.loadBufferedImage("images/maps/floor2/map.png");
        super.loadMapFromSpriteSheet(spriteSheet);
        setTileCollisions();
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

    private void setTileCollisions() { // Only if necessary (custom to each map)
        tileIds[map[2][10]].setCollision(true);
    }

    protected InteractiveObject[][] loadObjects() {
        // Load or generate the objects specific to this floor
        InteractiveObject[][] grid = new InteractiveObject[10][14]; // Initial grid of null objects

        // Add Interactive Objects to the grid (map)
        grid[2][12] = new Key(12*tileSize, 2*tileSize);
        grid[8][1] = new MagicCircle(1*tileSize, 8*tileSize);
        return grid;
    }

    public void createEntities() {
        Entity entity = new Enemy(panel, 3 * tileSize, 4 * tileSize, 4, this, "left");
        addEntity(entity);

        entity = new Enemy(panel, 5 * tileSize, 7 * tileSize, 4, this, "right");
        addEntity(entity);

        entity = new Enemy(panel, 12 * tileSize, 6 * tileSize, 4, this, "left");
        addEntity(entity);

        // Create Player (Created last so that player can be drawn on top of all other entities)
        entity = new Player(panel, 1 * tileSize, 1 * tileSize, 4, this, "right");
        addEntity(entity);
        this.player = (Player) entity; // Add player to control
    }
}
