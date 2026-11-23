package hu.rpg.domain;

public class FlameSwordDecorator extends EquipmentDecorator {
    private final int bonusFireDamage;

    public FlameSwordDecorator(int bonusFireDamage) {
        this(null, bonusFireDamage);
    }

    public FlameSwordDecorator(EquipmentDecorator wrappedEquipment, int bonusFireDamage) {
        super(wrappedEquipment);
        if (bonusFireDamage < 0) {
            throw new IllegalArgumentException("A bónusz tűzsebzés nem lehet negatív.");
        }
        this.bonusFireDamage = bonusFireDamage;
    }

    @Override
    public String getDescription() {
        String own = "Tüzes fegyver (+" + bonusFireDamage + " sebzés)";
        String prev = super.getDescription();
        return prev.isEmpty() ? own : prev + " + " + own;
    }

    @Override
    public int modifyOutgoingDamage(int baseDamage) {
        return super.modifyOutgoingDamage(baseDamage) + bonusFireDamage;
    }
}
