import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.*;

public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener {
    private GamePanel gamePanel;
    private HUDPanel hudPanel;

    public static final int tileSize = 64;

    public GameWindow() {
        setTitle("Escape The Labyrinth");
        setLayout(new BorderLayout());

        hudPanel = new HUDPanel(0, 0);
        gamePanel = new GamePanel(this, hudPanel);
        
        add(gamePanel, BorderLayout.WEST);
        add(hudPanel, BorderLayout.EAST);

        pack();

        addKeyListener(this);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        hudPanel.setGamePanel(gamePanel);

        System.out.println("Threads: " + Thread.activeCount());
        requestFocusInWindow();
    }

    // implement single method in ActionListener interface

	public void actionPerformed(ActionEvent e) {}


	// implement methods in KeyListener interface

	public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) gamePanel.movePlayer(0);
        else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) gamePanel.movePlayer(1);
        else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) gamePanel.movePlayer(2);
        else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) gamePanel.movePlayer(3);
        
    }

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}


	// implement methods in MouseListener interface

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}