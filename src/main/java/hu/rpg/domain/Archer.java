package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public class Archer extends Character {
    private final double critChance;

    public Archer(String name, int maxHp, int baseAttack, double critChance) {
        super(name, maxHp, 20, baseAttack);
        if (critChance < 0.0 || critChance > 1.0) {
            throw new IllegalArgumentException("A kritikus találati esélynek 0.0 és 1.0 között kell lennie.");
        }
        this.critChance = critChance;
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        validateCanAct(target);
        boolean isCrit = Math.random() < critChance;
        int rawDamage = isCrit ? getBaseAttack() * 2 : getBaseAttack();
        int totalDamage = calculateTotalAttackDamage(rawDamage);
        target.takeDamage(totalDamage);
        return totalDamage;
    }
}
