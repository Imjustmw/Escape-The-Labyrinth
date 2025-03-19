import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

public class EffectsManager {
    private static ArrayList<Effect> effects = new ArrayList<Effect>();
    private static ArrayList<Effect> pendingEffects = new ArrayList<Effect>();

    public static void addEffect(int x, int y, int width, int height, Animation anim, boolean abovePlayer) {
        Effect effect = new Effect(x, y, width, height, anim, abovePlayer);
        pendingEffects.add(effect);
    }

    public static void update() {
        // Add new effects before iterating
        effects.addAll(pendingEffects);
        pendingEffects.clear();

        Iterator<Effect> iterator = effects.iterator(); // Explicit type declaration
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            if (!effect.isActive()) {
                iterator.remove(); // Safely removes inactive effects
            } else {
                effect.update(); // Updates active effects
            }
        }
    }

    public static void drawBelow(Graphics2D g2) {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            if (!effect.isAbovePlayer())
                effect.draw(g2);
        }
    }

    public static void drawAbove(Graphics2D g2) {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            if (effect.isAbovePlayer())
                effect.draw(g2);
        }
    }
}
