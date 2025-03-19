import java.util.ArrayList;

public class LevelManager {
    private ArrayList<Map> levels;
    private int currentLevel;
    private GamePanel panel;
    private HUDPanel hud;

    public LevelManager(GamePanel panel, HUDPanel hud) {
        this.panel = panel;
        this.hud = hud;
        this.levels = new ArrayList<>();
        this.currentLevel = 0;
    }

    public Map getCurrentLevel() { return this.levels.get(currentLevel); }
    public int getCurrentLevelIndex() { return this.currentLevel + 1; }

    public void loadLevels() {
        levels.clear();
        levels.add(new Floor1(panel, hud));
        levels.add(new Floor2(panel, hud));
        levels.add(new Floor3(panel, hud));
    }

    public void nextLevel() {
        if (currentLevel < levels.size() - 1) {
            getCurrentLevel().removeEntities(); // Clear all entities on previous level
            currentLevel++;
            panel.loadLevel();
        } else {
            System.out.println("Game Complete!"); // Handle end of game
            hud.setStopGame(true);
            panel.stopGame();
        }
    }

    public void resetGame() {
        currentLevel = 0;
        panel.loadLevel();
    }

    public void stopGame() {
        for (Map level: levels) {
            level.resetMap();
        }
        currentLevel = 0;
    }
}
