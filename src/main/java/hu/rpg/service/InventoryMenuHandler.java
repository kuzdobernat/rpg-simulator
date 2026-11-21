package hu.rpg.ui;

import hu.rpg.domain.Character;
import hu.rpg.domain.EquipmentDecorator;
import hu.rpg.domain.Fighter;
import hu.rpg.domain.Inventory;
import hu.rpg.domain.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryMenuHandler {

    public void manageInventory(Scanner scanner, List<Fighter> roster) {
        List<Character> characters = new ArrayList<>();
        for (Fighter f : roster) {
            Character baseChar = unwrapCharacter(f);
            if (baseChar != null && !characters.contains(baseChar)) {
                characters.add(baseChar);
            }
        }

        if (characters.isEmpty()) {
            System.out.println("Nincs olyan karakter, akinek hátizsákja lenne.");
            return;
        }

        System.out.println("\nKarakterek hátizsákkal:");
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            Inventory<Item> inv = c.getInventory();
            System.out.printf(" [%d] %s (%s) - Súly: %.1f/%.1f kg, Tárgyak száma: %d%n",
                    i + 1, c.getName(), c.getClass().getSimpleName(),
                    inv.getCurrentWeight(), inv.getMaxWeight(), inv.getItems().size());
        }

        int charIdx = askIndex(scanner, "Válaszd ki a karakter sorszámát: ", characters.size());
        Character selected = characters.get(charIdx);
        Inventory<Item> inventory = selected.getInventory();

        boolean managing = true;
        while (managing) {
            System.out.printf("\n--- %s HÁTIZSÁKJA (%.1f/%.1f kg) ---%n",
                    selected.getName(), inventory.getCurrentWeight(), inventory.getMaxWeight());
            System.out.println("1. Tárgyak listázása (érték szerint csökkenő - Comparable)");
            System.out.println("2. Tárgyak listázása (súly szerint növekvő - Comparator)");
            System.out.println("3. Új tárgy hozzáadása");
            System.out.println("4. Tárgy elvétele");
            System.out.println("0. Vissza a főmenübe");
            System.out.print("Válassz műveletet: ");

            String action = scanner.nextLine().trim();
            switch (action) {
                case "1" -> printItemList(inventory.getItemsSortedByValue(), "Érték szerint rendezve");
                case "2" -> printItemList(inventory.getItemsSortedByWeight(), "Súly szerint rendezve");
                case "3" -> {
                    System.out.print("Add meg a tárgy nevét: ");
                    String itemName = scanner.nextLine().trim();
                    System.out.print("Add meg a tárgy súlyát (kg, pl. 1.5): ");
                    double weight = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                    System.out.print("Add meg a tárgy értékét (arany, egész szám): ");
                    int value = Integer.parseInt(scanner.nextLine().trim());

                    Item newItem = new Item(itemName, weight, value);
                    inventory.addItem(newItem);
                    System.out.println("Hozzáadva: " + newItem);
                }
                case "4" -> {
                    List<Item> currentItems = inventory.getItems();
                    if (currentItems.isEmpty()) {
                        System.out.println("A hátizsák már üres!");
                        break;
                    }
                    printItemList(currentItems, "Jelenlegi tárgyak");
                    int itemIdx = askIndex(scanner, "Add meg az elveendő tárgy sorszámát: ", currentItems.size());
                    Item removed = inventory.removeItem(itemIdx);
                    System.out.println("Eltávolítva a hátizsákból: " + removed);
                }
                case "0" -> managing = false;
                default -> System.out.println("Érvénytelen művelet!");
            }
        }
    }

    private void printItemList(List<Item> items, String title) {
        System.out.println("\n" + title + ":");
        if (items.isEmpty()) {
            System.out.println(" (üres)");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.printf(" [%d] %s%n", i + 1, items.get(i));
        }
    }

    private Character unwrapCharacter(Fighter fighter) {
        Fighter current = fighter;
        while (current instanceof EquipmentDecorator decorator) {
            current = decorator.getWrappedFighter();
        }
        return (current instanceof Character character) ? character : null;
    }

    private int askIndex(Scanner scanner, String prompt, int max) {
        System.out.print(prompt);
        int val = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (val < 0 || val >= max) {
            throw new IllegalArgumentException("Nincs ilyen sorszámú elem!");
        }
        return val;
    }
}
