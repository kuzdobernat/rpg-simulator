package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public class Rogue extends Character {
    private final double dodgeChance;

    public Rogue(String name, int maxHp, int baseAttack, double dodgeChance) {
        super(name, maxHp, 10, baseAttack);
        if (dodgeChance < 0.0 || dodgeChance > 0.8) {
            throw new IllegalArgumentException("A kitérési esélynek 0.0 és 0.8 között kell lennie.");
        }
        this.dodgeChance = dodgeChance;
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        validateCanAct(target);
        int totalDamage = calculateTotalAttackDamage(getBaseAttack() + 7);
        target.takeDamage(totalDamage);
        return totalDamage;
    }

    @Override
    public void takeDamage(int damage) {
        if (Math.random() < dodgeChance) {
            return;
        }
        super.takeDamage(damage);
    }
}
