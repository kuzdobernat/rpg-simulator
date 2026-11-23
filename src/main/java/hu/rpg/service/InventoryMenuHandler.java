package hu.rpg.service;

import hu.rpg.domain.Character;
import hu.rpg.domain.Fighter;
import hu.rpg.domain.Inventory;
import hu.rpg.domain.Item;
import hu.rpg.domain.ItemCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryMenuHandler {

    public void manageInventory(Scanner scanner, List<Fighter> roster) {
        List<Character> characters = new ArrayList<>();
        for (Fighter f : roster) {
            if (f instanceof Character character) {
                characters.add(character);
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
            System.out.printf(" [%d] %s (%s) - Súly: %.1f/%.1f kg, Tárgyak száma: %d (Fegyver: %d, Védelem: %d, Érték: %d)%n",
                    i + 1, c.getName(), c.getClass().getSimpleName(),
                    inv.getCurrentWeight(), inv.getMaxWeight(), inv.getItems().size(),
                    inv.countByCategory(ItemCategory.WEAPON),
                    inv.countByCategory(ItemCategory.DEFENSE),
                    inv.countByCategory(ItemCategory.TREASURE));
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
                    System.out.println("Kategóriák: 1 - Fegyver | 2 - Védelem | 3 - Érték");
                    System.out.print("Válassz kategóriát: ");
                    String catChoice = scanner.nextLine().trim();
                    ItemCategory category = switch (catChoice) {
                        case "1" -> ItemCategory.WEAPON;
                        case "2" -> ItemCategory.DEFENSE;
                        case "3" -> ItemCategory.TREASURE;
                        default -> throw new IllegalArgumentException("Ismeretlen tárgykategória!");
                    };

                    Item newItem = new Item(itemName, weight, value, category);
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

    private int askIndex(Scanner scanner, String prompt, int max) {
        System.out.print(prompt);
        int val = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (val < 0 || val >= max) {
            throw new IllegalArgumentException("Nincs ilyen sorszámú elem!");
        }
        return val;
    }
}
