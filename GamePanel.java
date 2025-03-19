import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

public class GamePanel extends JPanel implements Runnable {
    private GameWindow gameWindow;
    private boolean isRunning;
    private boolean isPaused;

    private SoundManager soundManager;
    private Thread gameThread;
    private LevelManager levelManager;
    private Map currentLevel;

    private BufferedImage imageBuffer;

    public GamePanel(GameWindow gameWindow, HUDPanel hud) {
        this.gameWindow = gameWindow;
        this.soundManager = SoundManager.getInstance();
        this.imageBuffer = new BufferedImage(GameWindow.tileSize * 14, GameWindow.tileSize * 10, BufferedImage.TYPE_INT_ARGB);
        this.levelManager = new LevelManager(this, hud);
        this.isRunning = false;
        this.isPaused = false;
        this.currentLevel = null;

        this.setPreferredSize(new Dimension(GameWindow.tileSize * 14, GameWindow.tileSize * 10));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
    }

    public boolean getIsRunning() { return this.isRunning; }
    public boolean getPaused() { return this.isPaused; }

    public void startGame(){ // Start Game Thread
        if (isRunning) return;
        
        isRunning = true;
        isPaused = false;

        levelManager.loadLevels(); // Initialize Game Levels

        levelManager.resetGame();

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void loadLevel() {
        currentLevel = levelManager.getCurrentLevel();
        currentLevel.resetMap(); // Reset Entities and Objects and Number of Moves Remaining
        currentLevel.updateHud(levelManager.getCurrentLevelIndex());
        currentLevel.createEntities(); // Create all entities for this level
        soundManager.playSound("background", true); // play background sound
        soundManager.setVolume("background", 0.75f);
        gameWindow.requestFocusInWindow(); // To receive Game Input
    }

    public void nextLevel() { levelManager.nextLevel(); }
    public void resetGame() { levelManager.resetGame(); }

    public boolean pauseGame() { // Pause Game Thread
        if (isRunning) isPaused = !isPaused;
        if (currentLevel != null) currentLevel.pause(isPaused);
        if (isPaused) soundManager.pauseAllSounds();
        else soundManager.resumeAllSounds();

        return isPaused;
    }

    public void stopGame() { // Stop Game Thread
        if (isPaused) pauseGame();
        isRunning = false;
        levelManager.stopGame();
        soundManager.stopAllSounds();

        // Draw a blank Frame
        Graphics2D backBuffer = (Graphics2D) imageBuffer.getGraphics();

        backBuffer.setColor(Color.BLACK);
        backBuffer.fillRect(0, 0, getWidth(), getHeight());

         // Swap buffers
         Graphics2D g2 = (Graphics2D) getGraphics();
         g2.drawImage(imageBuffer, 0, 0, getWidth(), getHeight(), null);
 
         // Cleanup
         backBuffer.dispose();
         g2.dispose();
    }

    public void movePlayer(int direction) {
        if (currentLevel == null || !isRunning || isPaused) return;
        Player p = currentLevel.getPlayer();
        if (p != null) p.move(direction);
    }

    public void run() { // Game Loop
        try {
            isRunning = true;
            while (isRunning) {
                if (!isPaused)
                    gameUpdate();
                gameRender();
                Thread.sleep(100/6); // 60 FPS
            }
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void gameUpdate() { // Update
        // Update Level (Map and Entities)
        if (currentLevel != null) currentLevel.update();

        // Update Special Effects
        EffectsManager.update();
    }

    public void gameRender() { // Double Buffer Rendering
        // Draw everything onto the back buffer
        Graphics2D backBuffer = (Graphics2D) imageBuffer.getGraphics();

        // Draw Background
        backBuffer.setColor(Color.BLACK);
        backBuffer.fillRect(0, 0, getWidth(), getHeight());

        // Draw Map
        if (currentLevel != null) currentLevel.draw(backBuffer);

        // Draw Special Effects Below Entities
        EffectsManager.drawBelow(backBuffer);

        // Draw all entities
        if (currentLevel != null) currentLevel.drawEntities(backBuffer);

        // Draw Special Effects Above Entities
        EffectsManager.drawAbove(backBuffer);

        // Swap buffers
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(imageBuffer, 0, 0, getWidth(), getHeight(), null);

        // Cleanup
        backBuffer.dispose();
        g2.dispose();
    }
}
