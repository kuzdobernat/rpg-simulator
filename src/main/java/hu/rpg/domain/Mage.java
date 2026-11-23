package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public class Mage extends Character {
    private final int spellPower;

    public Mage(String name, int maxHp, int mana, int baseAttack, int spellPower) {
        super(name, maxHp, mana, baseAttack);
        if (spellPower < 0) {
            throw new IllegalArgumentException("A varázserő nem lehet negatív.");
        }
        this.spellPower = spellPower;
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        validateCanAct(target);
        int rawDamage;
        if (getMana() >= 15) {
            consumeMana(15);
            rawDamage = getBaseAttack() + spellPower;
        } else {
            rawDamage = getBaseAttack();
        }
        int totalDamage = calculateTotalAttackDamage(rawDamage);
        target.takeDamage(totalDamage);
        return totalDamage;
    }
}
