package hu.rpg;

import hu.rpg.domain.*;
import hu.rpg.service.CombatEngine;
import hu.rpg.service.InvalidActionException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    @Test
    void testDecoratorIncreasesDamage() throws InvalidActionException {
        Warrior warrior = new Warrior("Teszt Harcos", 100, 10, 0);
        Fighter flameWarrior = new FlameSwordDecorator(warrior, 8);
        Mage target = new Mage("Célpont", 100, 30, 5, 10);

        int damage = flameWarrior.attack(target);

        assertEquals(23, damage);
        assertEquals(77, target.getHp());
    }

    @Test
    void testPaladinHealsOnAttack() throws InvalidActionException {
        Paladin paladin = new Paladin("Uther", 100, 20, 10, 8);
        paladin.takeDamage(30);
        Warrior dummy = new Warrior("Bábu", 100, 5, 0);

        paladin.attack(dummy);

        assertEquals(78, paladin.getHp());
        assertEquals(10, paladin.getMana());
    }

    @Test
    void testSimulateDuelProducesWinner() throws InvalidActionException {
        Warrior w1 = new Warrior("Harcos 1", 80, 15, 2);
        Mage m1 = new Mage("Mágus 1", 70, 45, 8, 20);
        CombatEngine engine = new CombatEngine();

        List<String> log = engine.simulateDuel(w1, m1);

        assertFalse(log.isEmpty());
        assertTrue(!w1.isAlive() || !m1.isAlive());
    }

    @Test
    void testSelfAttackThrowsException() {
        Warrior warrior = new Warrior("Önmagát támadó", 100, 10, 0);
        CombatEngine engine = new CombatEngine();

        assertThrows(InvalidActionException.class, () -> engine.executeTurn(warrior, warrior));
    }

    @Test
    void testInventoryAddRemoveAndSorting() {
        Inventory<Item> inventory = new Inventory<>(20.0);
        Item cheap = new Item("Kenyér", 0.5, 5);
        Item expensive = new Item("Rubin", 0.2, 500);

        inventory.addItem(cheap);
        inventory.addItem(expensive);

        List<Item> sortedByValue = inventory.getItemsSortedByValue();
        assertEquals("Rubin", sortedByValue.get(0).getName());

        List<Item> sortedByWeight = inventory.getItemsSortedByWeight();
        assertEquals("Rubin", sortedByWeight.get(0).getName());

        Item removed = inventory.removeItem(0);
        assertEquals("Kenyér", removed.getName());
        assertEquals(1, inventory.getItems().size());
    }
}
