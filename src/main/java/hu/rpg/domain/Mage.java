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
        if (getMana() >= 15) {
            consumeMana(15);
            int magicDamage = getBaseAttack() + spellPower;
            target.takeDamage(magicDamage);
            return magicDamage;
        } else {
            int staffDamage = getBaseAttack();
            target.takeDamage(staffDamage);
            return staffDamage;
        }
    }
}
