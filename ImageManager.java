import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class ImageManager {
    public static Image loadImage(String path) {
        return new ImageIcon(path).getImage();
    }

    public static BufferedImage loadBufferedImage(String path) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Error loading image: " + path + '\n' + e.getMessage());
        }
        return bi;
    }

    public static BufferedImage copyImage(BufferedImage bi) {
        if (bi == null)
            return null;
        
        int imWidth = bi.getWidth();
        int imHeight = bi.getHeight();

        BufferedImage copy = new BufferedImage(imWidth, imHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = copy.createGraphics();

        // copy image
        g2.drawImage(bi, 0, 0, null);
        g2.dispose();

        return copy;
    }

    public static BufferedImage flipHorizontally(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, image.getType());
        Graphics2D g2 = flipped.createGraphics();
        g2.drawImage(image, 0, 0, w, h, w, 0, 0, h, null);
        g2.dispose();
        return flipped;
    }

    public static boolean isBlankImage(BufferedImage image) { //  A little magic function to check if the image is blank
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF; // Extract alpha channel

                if (alpha != 0) {
                    return false; // Found a non-transparent pixel, so it's NOT blank
                }
            }
        }

        return true; // All pixels are fully transparent
    }
}
