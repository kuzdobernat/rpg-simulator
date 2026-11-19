package hu.rpg.domain;

public class MagicShieldDecorator extends EquipmentDecorator {
    private final int damageReduction;

    public MagicShieldDecorator(Fighter wrappedFighter, int damageReduction) {
        super(wrappedFighter);
        if (damageReduction < 0) {
            throw new IllegalArgumentException("A sebzéscsökkentés nem lehet negatív.");
        }
        this.damageReduction = damageReduction;
    }

    @Override
    public String getName() {
        return wrappedFighter.getName() + " [Mágikus Pajzs]";
    }

    @Override
    public void takeDamage(int damage) {
        int mitigated = Math.max(0, damage - damageReduction);
        super.takeDamage(mitigated);
    }
}
