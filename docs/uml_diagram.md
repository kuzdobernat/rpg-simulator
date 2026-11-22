---

### `docs/uml_diagram.md`

```markdown
# UML Osztálydiagram

```mermaid
classDiagram
    direction LR

    class Fighter {
        <<interface>>
        +getName() String
        +getHp() int
        +getMaxHp() int
        +isAlive() boolean
        +attack(Fighter target) int
        +takeDamage(int damage) void
        +heal(int amount) void
    }

    class Character {
        <<abstract>>
        -name: String
        -hp: int
        -maxHp: int
        -mana: int
        -baseAttack: int
        -inventory: Inventory~Item~
        #Character(String name, int maxHp, int mana, int baseAttack)
        +getName() String
        +getHp() int
        +getMaxHp() int
        +getMana() int
        +getBaseAttack() int
        +getInventory() Inventory~Item~
        +isAlive() boolean
        +takeDamage(int damage) void
        +heal(int amount) void
        #consumeMana(int amount) void
        #validateCanAct(Fighter target) void
        +attack(Fighter target)* int
    }

    class Warrior {
        -armor: int
        +Warrior(String name, int maxHp, int baseAttack, int armor)
        +attack(Fighter target) int
        +takeDamage(int damage) void
        +getArmor() int
    }

    class Mage {
        -spellPower: int
        +Mage(String name, int maxHp, int mana, int baseAttack, int spellPower)
        +attack(Fighter target) int
    }

    class Archer {
        -critChance: double
        +Archer(String name, int maxHp, int baseAttack, double critChance)
        +attack(Fighter target) int
    }

    class Rogue {
        -dodgeChance: double
        +Rogue(String name, int maxHp, int baseAttack, double dodgeChance)
        +attack(Fighter target) int
        +takeDamage(int damage) void
    }

    class Paladin {
        -holyPower: int
        +Paladin(String name, int maxHp, int mana, int baseAttack, int holyPower)
        +attack(Fighter target) int
    }

    class EquipmentDecorator {
        <<abstract>>
        #wrappedFighter: Fighter
        #EquipmentDecorator(Fighter wrappedFighter)
        +getWrappedFighter() Fighter
        +getName() String
        +getHp() int
        +getMaxHp() int
        +isAlive() boolean
        +attack(Fighter target) int
        +takeDamage(int damage) void
        +heal(int amount) void
    }

    class FlameSwordDecorator {
        -bonusFireDamage: int
        +FlameSwordDecorator(Fighter wrappedFighter, int bonusFireDamage)
        +getName() String
        +attack(Fighter target) int
    }

    class MagicShieldDecorator {
        -damageReduction: int
        +MagicShieldDecorator(Fighter wrappedFighter, int damageReduction)
        +getName() String
        +takeDamage(int damage) void
    }

    class Item {
        -name: String
        -weight: double
        -value: int
        +Item(String name, double weight, int value)
        +getName() String
        +getWeight() double
        +getValue() int
        +compareTo(Item other) int
    }

    class Inventory~T extends Item~ {
        -items: List~T~
        -maxWeight: double
        +Inventory(double maxWeight)
        +addItem(T item) void
        +removeItem(int index) T
        +removeItem(T item) boolean
        +getCurrentWeight() double
        +getMaxWeight() double
        +getItemsSortedByValue() List~T~
        +getItemsSortedByWeight() List~T~
        +getItems() List~T~
    }

    class CombatEngine {
        +executeTurn(Fighter attacker, Fighter defender) String
        +simulateDuel(Fighter first, Fighter second) List~String~
    }

    class InvalidActionException {
        <<exception>>
        +InvalidActionException(String message)
    }

    class InventoryMenuHandler {
        +manageInventory(Scanner scanner, List~Fighter~ roster) void
    }

    class GameRepository {
        -gson: Gson
        +GameRepository()
        +saveWarrior(Warrior warrior, String filePath) void
        +loadWarrior(String filePath) Warrior
    }

    class Main {
        +main(String[] args)$ void
    }

    Fighter <|.. Character
    Character <|-- Warrior
    Character <|-- Mage
    Character <|-- Archer
    Character <|-- Rogue
    Character <|-- Paladin

    Fighter <|.. EquipmentDecorator
    EquipmentDecorator o-- "1" Fighter : wrappedFighter
    EquipmentDecorator <|-- FlameSwordDecorator
    EquipmentDecorator <|-- MagicShieldDecorator

    Character *-- "1" Inventory : inventory
    Inventory o-- "*" Item : items

    Main ..> CombatEngine : uses
    Main ..> GameRepository : uses
    Main ..> InventoryMenuHandler : uses
    Main ..> Fighter : manages roster
    CombatEngine ..> Fighter : fights
    CombatEngine ..> InvalidActionException : throws
    Fighter ..> InvalidActionException : throws
    InventoryMenuHandler ..> Character : unwraps
    InventoryMenuHandler ..> Inventory : manages
    GameRepository ..> Warrior : serializes