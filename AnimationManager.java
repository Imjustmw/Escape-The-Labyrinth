import java.awt.image.BufferedImage;

public class AnimationManager {
    public static Animation createAnimation(String path, boolean loop, int rows, int cols, int duration) {
        Animation animation = new Animation(loop);
        BufferedImage spriteSheet = ImageManager.loadBufferedImage(path);
        animation.addFramesFromSpriteSheet(spriteSheet, rows, cols, duration);
        return animation;
    }

    public static Animation createFlippedAnimation(String path, boolean loop, int rows, int cols, int duration) {
        Animation animation = new Animation(loop);
        BufferedImage spriteSheet = ImageManager.loadBufferedImage(path);
        int frameWidth = spriteSheet.getWidth() / cols;
        int frameHeight = spriteSheet.getHeight() / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                BufferedImage frame = spriteSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight);
                BufferedImage flippedFrame = ImageManager.flipHorizontally(frame); // Flip each frame separately
                animation.addFrame(flippedFrame, duration);
            }
        }

        return animation;
    }
}
