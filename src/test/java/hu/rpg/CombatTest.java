package hu.rpg;

import hu.rpg.domain.*;
import hu.rpg.persistence.GameRepository;
import hu.rpg.service.CombatEngine;
import hu.rpg.service.InvalidActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    @Test
    void testEquipmentDecoratorIncreasesDamage() throws InvalidActionException {
        Warrior warrior = new Warrior("Teszt Harcos", 100, 10, 0);
        warrior.getInventory().addItem(new Item("Kard", 2.0, 100, ItemCategory.WEAPON));
        warrior.equip(new FlameSwordDecorator(warrior.getEquipment(), 8));
        Mage target = new Mage("Célpont", 100, 30, 5, 10);

        int damage = warrior.attack(target);

        assertEquals(23, damage);
        assertEquals(77, target.getHp());
    }

    @Test
    void testStackedEquipmentDecoratorsAndCounting() {
        Mage mage = new Mage("Mágus", 100, 30, 5, 10);
        mage.getInventory().addItem(new Item("Varázsbot", 1.5, 120, ItemCategory.WEAPON));
        mage.getInventory().addItem(new Item("Rúna köpeny", 1.0, 90, ItemCategory.DEFENSE));
        mage.getInventory().addItem(new Item("Aranykehely", 0.8, 300, ItemCategory.TREASURE));

        assertEquals(1, mage.getInventory().countByCategory(ItemCategory.WEAPON));
        assertEquals(1, mage.getInventory().countByCategory(ItemCategory.DEFENSE));
        assertEquals(1, mage.getInventory().countByCategory(ItemCategory.TREASURE));

        mage.equip(new FlameSwordDecorator(8));
        mage.equip(new MagicShieldDecorator(mage.getEquipment(), 6));

        assertEquals(1, mage.getEquipment().countInstancesOf(FlameSwordDecorator.class));
        assertEquals(1, mage.getEquipment().countInstancesOf(MagicShieldDecorator.class));

        mage.takeDamage(20);
        assertEquals(86, mage.getHp());
        assertTrue(mage.getEquipment().getDescription().contains("Tüzes Kard"));
        assertTrue(mage.getEquipment().getDescription().contains("Mágikus Pajzs"));
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
        Item cheap = new Item("Kenyér", 0.5, 5, ItemCategory.TREASURE);
        Item expensive = new Item("Rubin", 0.2, 500, ItemCategory.TREASURE);

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

    @Test
    void testSaveAndLoadEntireRosterWithDecorators(@TempDir Path tempDir) throws IOException, InvalidActionException {
        Warrior warrior = new Warrior("Varian", 130, 16, 4);
        warrior.getInventory().addItem(new Item("Gyógyital", 0.5, 50, ItemCategory.TREASURE));
        warrior.getInventory().addItem(new Item("Kard", 2.0, 150, ItemCategory.WEAPON));
        warrior.getInventory().addItem(new Item("Pajzs", 3.0, 140, ItemCategory.DEFENSE));
        warrior.equip(new FlameSwordDecorator(8));
        warrior.equip(new MagicShieldDecorator(warrior.getEquipment(), 3));

        Mage mage = new Mage("Khad'gar", 95, 60, 8, 24);
        warrior.attack(mage);

        List<Fighter> originalRoster = List.of(warrior, mage);
        Path saveFile = tempDir.resolve("roster_save.json");

        GameRepository repo = new GameRepository();
        repo.saveRoster(originalRoster, saveFile.toString());
        List<Fighter> loadedRoster = repo.loadRoster(saveFile.toString());

        assertEquals(2, loadedRoster.size());
        Warrior loadedWarrior = assertInstanceOf(Warrior.class, loadedRoster.get(0));
        assertInstanceOf(MagicShieldDecorator.class, loadedWarrior.getEquipment());
        assertInstanceOf(FlameSwordDecorator.class, loadedWarrior.getEquipment().getWrappedEquipment());
        assertEquals(mage.getHp(), loadedRoster.get(1).getHp());
        assertEquals(3, loadedWarrior.getInventory().getItems().size());
        assertEquals(ItemCategory.WEAPON, loadedWarrior.getInventory().getItems().get(1).getCategory());
    }
}
