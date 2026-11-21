package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public abstract class EquipmentDecorator implements Fighter {
    protected final Fighter wrappedFighter;

    protected EquipmentDecorator(Fighter wrappedFighter) {
        if (wrappedFighter == null) {
            throw new IllegalArgumentException("A becsomagolt harcos nem lehet null.");
        }
        this.wrappedFighter = wrappedFighter;
    }
    public Fighter getWrappedFighter() {
        return wrappedFighter;
    }
    @Override
    public String getName() {
        return wrappedFighter.getName();
    }

    @Override
    public int getHp() {
        return wrappedFighter.getHp();
    }

    @Override
    public int getMaxHp() {
        return wrappedFighter.getMaxHp();
    }

    @Override
    public boolean isAlive() {
        return wrappedFighter.isAlive();
    }

    @Override
    public int attack(Fighter target) throws InvalidActionException {
        return wrappedFighter.attack(target);
    }

    @Override
    public void takeDamage(int damage) {
        wrappedFighter.takeDamage(damage);
    }

    @Override
    public void heal(int amount) {
        wrappedFighter.heal(amount);
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d)", getName(), getHp(), getMaxHp());
    }
}
