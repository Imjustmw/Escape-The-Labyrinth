import java.awt.*;
import javax.swing.*;
import java.io.InputStream;

public class HUDPanel extends JPanel {
    private GamePanel gamePanel;
    private JLabel levelLabel;
    private JLabel movesLabel;
    private JLabel gifLabel;
    private JButton restartButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JButton playButton;
    private Font customFont;
    
    public HUDPanel(int level, int maxMoves) {
        this.setPreferredSize(new Dimension(200, GameWindow.tileSize * 10));
        this.setBackground(Color.decode("#191919"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Arrange elements vertically

        // Load custom font
        try {
            InputStream fontStream = getClass().getResourceAsStream("fonts/Minecraft.ttf");
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch (Exception e) {
            e.printStackTrace();
            this.customFont = new Font("Arial", Font.PLAIN, 20); // Fallback font
        }

        // Level Label
        levelLabel = new JLabel("");
        levelLabel.setFont(customFont.deriveFont(Font.PLAIN, 22));
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center text

        // Moves Label
        movesLabel = new JLabel("");
        movesLabel.setFont(customFont.deriveFont(Font.PLAIN, 20));
        movesLabel.setForeground(Color.WHITE);
        movesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button Panel to hold buttons side-by-side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        buttonPanel.setOpaque(false); // Transparent background

        // Play Button
        playButton = new JButton("Start");
        playButton.setFont(customFont.deriveFont(Font.PLAIN, 22));
        playButton.setBackground(Color.WHITE);
        playButton.setForeground(Color.BLACK);
        playButton.setFocusable(false);
        playButton.addActionListener(e -> playGame());

        // Restart Button
        restartButton = new JButton("Restart");
        restartButton.setFont(customFont.deriveFont(Font.PLAIN, 18));
        restartButton.setBackground(Color.WHITE);
        restartButton.setForeground(Color.BLACK);
        restartButton.setFocusable(false); // Prevent focus (Interupts Gameplay Inputs)
        restartButton.addActionListener(e -> restartLevel());

        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.setFont(customFont.deriveFont(Font.PLAIN, 18));
        resetButton.setBackground(Color.WHITE);
        resetButton.setForeground(Color.BLACK);
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> resetGame());

        // Pause Button
        pauseButton = new JButton("Pause");
        pauseButton.setFont(customFont.deriveFont(Font.PLAIN, 18));
        pauseButton.setBackground(Color.WHITE);
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> pauseGame());
        
        // Add buttons to panel
        buttonPanel.add(playButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(pauseButton);

        // Load and add the GIF
        gifLabel = new JLabel();
        ImageIcon tutorial = new ImageIcon(getClass().getResource("images/effects/Controls.gif"));
        gifLabel.setIcon(tutorial);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gifLabel.setVisible(false);

        // Layout
        add(Box.createVerticalGlue());
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
        add(levelLabel);
        add(Box.createVerticalStrut(20)); // Add spacing
        add(movesLabel);
        add(Box.createVerticalStrut(20)); // Add spacing
        add(gifLabel);
        add(Box.createVerticalGlue()); // Keep everything centered
    }

    private void playGame() {
        if (gamePanel.getIsRunning()) { // Stop Game
            gamePanel.stopGame();
            setStopGame(true);
        } else { // Start Game
            gamePanel.startGame();
            setStopGame(false);
        }
    }

    private void restartLevel() {
        if (gamePanel.getPaused()) pauseGame(); // Unpause
        gamePanel.loadLevel();
    }

    private void resetGame() {
        if (gamePanel.getPaused()) pauseGame(); // Unpause
        gamePanel.resetGame();
    }

    private void pauseGame() {
        String text = gamePanel.pauseGame()? "Resume" : "Pause";
        pauseButton.setText(text);
    }

    public void setStopGame(boolean stop) {
        if (stop) {
            playButton.setText("Start");
            pauseButton.setText("Pause");
            gifLabel.setVisible(false);
            movesLabel.setText("");
            levelLabel.setText("");
        } else {
            playButton.setText(" Stop ");
            pauseButton.setText("Pause");
            gifLabel.setVisible(true);
        }
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void updateMoves(int moves) {
        movesLabel.setText("Moves Left: " + moves);
    }

    public void updateLevel(int level) {
        levelLabel.setText("Level: " + level);
    }
}