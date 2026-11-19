package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public class FlameSwordDecorator extends EquipmentDecorator {
    private final int bonusFireDamage;

    public FlameSwordDecorator(Fighter wrappedFighter, int bonusFireDamage) {
        super(wrappedFighter);
        if (bonusFireDamage < 0) {
            throw new IllegalArgumentException("A bónusz tűzsebzés nem lehet negatív.");
        }
        this.bonusFireDamage = bonusFireDamage;
    }

    @Override
    public String getName() {
        return wrappedFighter.getName() + " [Tüzes Kard]";
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        int baseDamage = super.attack(target);
        if (target.isAlive()) {
            target.takeDamage(bonusFireDamage);
        }
        return baseDamage + bonusFireDamage;
    }
}
