package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public interface Fighter {
    String getName();
    int getHp();
    int getMaxHp();
    boolean isAlive();
    int attack(Fighter target) throws InvalidActionException;
    void takeDamage(int damage);
    void heal(int amount);
}
