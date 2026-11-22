package hu.rpg;

import hu.rpg.domain.*;
import hu.rpg.persistence.GameRepository;
import hu.rpg.service.CombatEngine;
import hu.rpg.service.InvalidActionException;
import hu.rpg.service.InventoryMenuHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String SAVE_FILE = "savegame.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CombatEngine engine = new CombatEngine();
        GameRepository repository = new GameRepository();
        InventoryMenuHandler inventoryMenu = new InventoryMenuHandler();

        List<Fighter> roster = new ArrayList<>();
        Warrior varian = new Warrior("Varian", 130, 16, 4);
        varian.getInventory().addItem(new Item("Gyógyital", 0.5, 50));
        varian.getInventory().addItem(new Item("Acélsisak", 3.2, 120));

        roster.add(varian);
        roster.add(new Mage("Khad'gar", 95, 60, 8, 24));
        roster.add(new Archer("Alleria", 105, 15, 0.35));
        roster.add(new Rogue("Valeera", 100, 14, 0.30));
        roster.add(new Paladin("Uther", 140, 40, 13, 10));

        System.out.println("=== RPG ARÉNA SZIMULÁTOR ===");
        boolean running = true;

        while (running) {
            System.out.println("\n--- FŐMENÜ ---");
            System.out.println("1. Szereplők listázása");
            System.out.println("2. Új szereplő létrehozása");
            System.out.println("3. Felszerelés adása egy szereplőnek (Decorator)");
            System.out.println("4. Egyetlen támadás szimulálása (választható támadó és célpont)");
            System.out.println("5. Teljes párbaj végsőkig (választható 2 szereplő)");
            System.out.println("6. Szereplő gyógyítása (pihenés)");
            System.out.println("7. Hátizsák kezelése (listázás/rendezés, tárgy hozzáadása, tárgy elvétele)");
            System.out.println("8. Összes karakter állapotának mentése JSON-be");
            System.out.println("9. Összes karakter állapotának betöltése JSON-ből");
            System.out.println("0. Kilépés");
            System.out.print("Válassz menüpontot: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> printRoster(roster);
                    case "2" -> createNewCharacter(scanner, roster);
                    case "3" -> equipCharacter(scanner, roster);
                    case "4" -> {
                        printRoster(roster);
                        int attackerIdx = askIndex(scanner, "Válaszd ki a TÁMADÓ sorszámát: ", roster.size());
                        int defenderIdx = askIndex(scanner, "Válaszd ki a VÉDŐ sorszámát: ", roster.size());
                        System.out.println(engine.executeTurn(roster.get(attackerIdx), roster.get(defenderIdx)));
                    }
                    case "5" -> {
                        printRoster(roster);
                        int firstIdx = askIndex(scanner, "Válaszd ki az 1. HARCOS sorszámát: ", roster.size());
                        int secondIdx = askIndex(scanner, "Válaszd ki a 2. HARCOS sorszámát: ", roster.size());
                        List<String> duelLog = engine.simulateDuel(roster.get(firstIdx), roster.get(secondIdx));
                        duelLog.forEach(System.out::println);
                    }
                    case "6" -> {
                        printRoster(roster);
                        int idx = askIndex(scanner, "Kit szeretnél meggyógyítani? Sorszám: ", roster.size());
                        Fighter f = roster.get(idx);
                        f.heal(40);
                        System.out.println(f.getName() + " gyógyult! Jelenlegi HP: " + f.getHp() + "/" + f.getMaxHp());
                    }
                    case "7" -> inventoryMenu.manageInventory(scanner, roster);
                    case "8" -> {
                        repository.saveRoster(roster, SAVE_FILE);
                        System.out.println("Sikeresen elmentve " + roster.size() + " szereplő állapota a(z) " + SAVE_FILE + " fájlba!");
                    }
                    case "9" -> {
                        List<Fighter> loadedRoster = repository.loadRoster(SAVE_FILE);
                        if (loadedRoster.isEmpty()) {
                            System.out.println("A mentési fájl üres, a jelenlegi lista megmaradt.");
                        } else {
                            roster.clear();
                            roster.addAll(loadedRoster);
                            System.out.println("Sikeresen betöltve " + roster.size() + " szereplő állapota!");
                            printRoster(roster);
                        }
                    }
                    case "0" -> running = false;
                    default -> System.out.println("Érvénytelen menüpont!");
                }
            } catch (InvalidActionException | IllegalArgumentException | IllegalStateException e) {
                System.out.println("[HIBA]: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("[FÁJL HIBA]: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printRoster(List<Fighter> roster) {
        System.out.println("\nElérhető szereplők:");
        for (int i = 0; i < roster.size(); i++) {
            Fighter f = roster.get(i);
            String status = f.isAlive() ? "ÉL" : "HALOTT";
            System.out.printf(" [%d] %s (%s)%n", i + 1, f, status);
        }
    }

    private static int askIndex(Scanner scanner, String prompt, int max) {
        System.out.print(prompt);
        int val = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (val < 0 || val >= max) {
            throw new IllegalArgumentException("Nincs ilyen sorszámú szereplő!");
        }
        return val;
    }

    private static void createNewCharacter(Scanner scanner, List<Fighter> roster) {
        System.out.println("Kasztok: 1-Harcos, 2-Mágus, 3-Íjász, 4-Orgyilkos, 5-Paladin");
        System.out.print("Válassz kasztot: ");
        String type = scanner.nextLine().trim();
        System.out.print("Add meg a karakter nevét: ");
        String name = scanner.nextLine().trim();

        Fighter created = switch (type) {
            case "1" -> new Warrior(name, 130, 15, 4);
            case "2" -> new Mage(name, 90, 60, 8, 25);
            case "3" -> new Archer(name, 105, 15, 0.35);
            case "4" -> new Rogue(name, 100, 14, 0.30);
            case "5" -> new Paladin(name, 135, 40, 13, 10);
            default -> throw new IllegalArgumentException("Ismeretlen kaszt!");
        };
        roster.add(created);
        System.out.println("Sikeresen létrehozva: " + created);
    }

    private static void equipCharacter(Scanner scanner, List<Fighter> roster) {
        printRoster(roster);
        int idx = askIndex(scanner, "Melyik szereplő kapjon felszerelést? Sorszám: ", roster.size());
        System.out.println("1 - Tüzes Kard (+8 tűzsebzés) | 2 - Mágikus Pajzs (-5 bekapott sebzés)");
        System.out.print("Válassz tárgyat: ");
        String itemChoice = scanner.nextLine().trim();

        Fighter target = roster.get(idx);
        Fighter decorated = switch (itemChoice) {
            case "1" -> new FlameSwordDecorator(target, 8);
            case "2" -> new MagicShieldDecorator(target, 5);
            default -> throw new IllegalArgumentException("Ismeretlen felszerelés!");
        };
        roster.set(idx, decorated);
        System.out.println("Felszerelve: " + decorated.getName());
    }
}
