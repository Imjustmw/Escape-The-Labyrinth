import java.awt.Image;

// Data Structure for Tile object
public class Tile {
    private Image image;
    private boolean isCollidable;

    public Tile(Image image, boolean isCollidable) {
        this.image = image;
        this.isCollidable = isCollidable;
    }

    public Image getImage() { return image; }
    public boolean isCollidable() { return isCollidable; }
    public void setCollision(boolean collide) { this.isCollidable = collide; }
}
