package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public class Warrior extends Character {
    private final int armor;

    public Warrior(String name, int maxHp, int baseAttack, int armor) {
        super(name, maxHp, 0, baseAttack);
        if (armor < 0) {
            throw new IllegalArgumentException("A páncél értéke nem lehet negatív.");
        }
        this.armor = armor;
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        validateCanAct(target);
        int damage = getBaseAttack() + 5;
        target.takeDamage(damage);
        return damage;
    }

    @Override
    public void takeDamage(int damage) {
        int reducedDamage = Math.max(1, damage - armor);
        super.takeDamage(reducedDamage);
    }

    public int getArmor() {
        return armor;
    }
}
