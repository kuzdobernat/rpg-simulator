package hu.rpg.domain;

public class MagicShieldDecorator extends EquipmentDecorator {
    private final int damageReduction;

    public MagicShieldDecorator(int damageReduction) {
        this(null, damageReduction);
    }

    public MagicShieldDecorator(EquipmentDecorator wrappedEquipment, int damageReduction) {
        super(wrappedEquipment);
        if (damageReduction < 0) {
            throw new IllegalArgumentException("A sebzéscsökkentés nem lehet negatív.");
        }
        this.damageReduction = damageReduction;
    }

    @Override
    public String getDescription() {
        String own = "Mágikus védekezés (-" + damageReduction + " bekapott sebzés)";
        String prev = super.getDescription();
        return prev.isEmpty() ? own : prev + " + " + own;
    }

    @Override
    public int modifyIncomingDamage(int incomingDamage) {
        int mitigated = super.modifyIncomingDamage(incomingDamage);
        return Math.max(0, mitigated - damageReduction);
    }
}
