public class Key extends InteractiveObject {
    public Key(int x,int y) {
        super(x,y);
        addAnimations();
        setState("idle");
    }

    public void addAnimations() {
        animations.put("idle", AnimationManager.createAnimation("images/objects/key.png", true, 4, 4, 80));
    }

    public void interact(Player player) {
        if (isActive) {
            isActive = false;
            player.setPermit(true);

            // Collect Effect
            Animation collect_anim = AnimationManager.createFlippedAnimation("images/effects/key_collect.png", false, 1, 10, 65);
            EffectsManager.addEffect(x, y, GameWindow.tileSize, GameWindow.tileSize, collect_anim, true);

            // Play Sound Effect
            soundManager.playSound("key", false);
        }
    }
}
