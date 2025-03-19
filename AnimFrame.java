import java.awt.Image;

public class AnimFrame {
    private Image image;
    private long endTime;

    public AnimFrame(Image image, long endTime) {
        this.image = image;
        this.endTime = endTime;
    }

    public Image getImage() { return image; }
    public long getEndTime() { return endTime; }
}
