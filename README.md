# RPG Harci Szimuláció (Java OOP Projekt)

## 1. Projekt összefoglaló (Problem Statement)
A projekt célja egy körökre osztott, stratégiai RPG harci szimulátor megvalósítása Java nyelven. A játékos többféle karakterosztályból (Harcos, Mágus, Íjász, Orgyilkos, Lovag/Paladin) válogathat, új hősöket hozhat létre, felszerelésekkel erősítheti őket, és szabadon kiválaszthatja, hogy az arénában ki ki ellen harcoljon.

## 2. Előfeltételek (Prerequisites)
- Java JDK 17 vagy újabb
- Apache Maven 3.8+

## 3. Fordítás, Tesztelés és Futtatás
A projekt gyökérmappájából futtatható Maven parancsok:

```bash
# Fordítás és JUnit tesztek futtatása
mvn clean test

# Alkalmazás indítása
mvn compile exec:java
```

## 4. Választott Design Minta: Decorator
- **Probléma:** A karaktereket futásidőben szeretnénk különféle fegyverekkel és páncélokkal felruházni anélkül, hogy minden kombinációra külön alosztályt kellene létrehozni.
- **Résztvevő osztályok:**
  - `Fighter` (Komponens interfész)
  - `Character` és leszármazottai: `Warrior`, `Mage`, `Archer`, `Rogue`, `Paladin` (Konkrét komponensek)
  - `EquipmentDecorator` (Absztrakt dekorátor)
  - `FlameSwordDecorator`, `MagicShieldDecorator` (Konkrét dekorátorok)
- **Előny:** A karakterek sebzése és védelme dinamikusan, futásidőben bővíthető és egymásba ágyazható az Open/Closed elv betartásával.

## 5. Megvalósított OOP Követelmények
- **Encapsulation:** Privát mezők, validált konstruktorok és kontrollált getter/setter metódusok.
- **Abstraction:** `Fighter` interfész és `Character` absztrakt ősosztály.
- **Inheritance & Polymorphism:** 5 különböző karakterkaszt (`Warrior`, `Mage`, `Archer`, `Rogue`, `Paladin`) egyedi támadási, kritikus sebzési, védekezési és öngyógyító mechanikával.
- **Collections & Generics:** Saját generikus `Inventory<T extends Item>` osztály `Comparable<Item>` és `Comparator` alapú rendezéssel, valamint szereplőlista (`List<Fighter>`).
- **Exceptions:** Saját `InvalidActionException` a domain szabályok (pl. halott karakter cselekvése, önmaga megtámadása) kikényszerítésére.
- **Persistence:** JSON alapú mentés és betöltés `try-with-resources` használatával (`GameRepository`).
