package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public class Paladin extends Character {
    private final int holyPower;

    public Paladin(String name, int maxHp, int mana, int baseAttack, int holyPower) {
        super(name, maxHp, mana, baseAttack);
        if (holyPower < 0) {
            throw new IllegalArgumentException("A szent erő nem lehet negatív.");
        }
        this.holyPower = holyPower;
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        validateCanAct(target);
        int damage = getBaseAttack() + 3;
        target.takeDamage(damage);

        // Ha van elég manája, támadás közben gyógyítja magát
        if (getMana() >= 10) {
            consumeMana(10);
            heal(holyPower);
        }
        return damage;
    }
}
