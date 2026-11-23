# RPG Harci Szimuláció (`rpg-simulator`)

Java objektumorientált (OOP) konzolos harci szimulátor alkalmazás Maven build rendszerrel és JUnit 5 tesztekkel.

## 1\. Problémaállapot (Problem Statement)

* **Felhasználók:** Játékosok és a harci mechanikát vizsgáló hallgatók/fejlesztők.
* **Cél:** Különböző kasztú karakterek (`Warrior`, `Mage`, `Archer`, `Rogue`, `Paladin`) létrehozása, felszerelésekkel való bővítése, hátizsákjuk kezelése, valamint tetszőleges szereplők közötti körökre osztott támadások vagy teljes párbajok szimulálása.
* **Megoldott probléma:** Olyan bővíthető, jól szeparált tartományi és szolgáltatásréteget biztosít, amelyben a karakterek egységes `Fighter` szerződésen keresztül működnek, a felszerelések önálló `EquipmentDecorator` öröklési láncban bővíthetők, a hibás műveleteket egyedi kivétel kezeli, a hátizsák rendezhető, és a teljes névsor állapota JSON fájlba menthető/visszatölthető.

## 2\. Előfeltételek (Prerequisites)

* **Java JDK:** 17 vagy újabb
* **Apache Maven:** 3.8+

## 3\. Fordítás, Tesztelés és Futtatás

A projekt gyökérmappájából (`rpg-simulator/`) az alábbi parancsok futtathatók:

```bash
# Fordítás és JUnit 5 tesztek futtatása
mvn clean test

# Alkalmazás futtatása konzolon
mvn compile exec:java
```

## 4\. Használat (Sample Usage)

Indítás után az alábbi főmenü érhető el:

1. **Szereplők listázása** – kiírja a névsort (`roster`), az aktuális HP/Mana értékeket, az aktív felszereléseket és az élő/halott állapotot.
2. **Új szereplő létrehozása** – `Warrior`, `Mage`, `Archer`, `Rogue` vagy `Paladin` felvétele a listába.
3. **Felszerelés adása egy szereplőnek (Decorator)** – futásidőben `FlameSwordDecorator` vagy `MagicShieldDecorator` réteggel ruházza fel a karaktert.
4. **Egyetlen támadás szimulálása** – tetszőleges támadó és célpont kiválasztása egy akcióra.
5. **Teljes párbaj végsőkig** – két kiválasztott szereplő körökre osztott összecsapása (`simulateDuel`).
6. **Szereplő gyógyítása (pihenés)** – HP visszatöltése a maximális értékig (`heal`).
7. **Hátizsák kezelése** – tárgyak listázása érték (`Comparable`) vagy súly (`Comparator`) szerint rendezve, új `Item` hozzáadása súlylimittel, illetve tárgy elvétele.
8. **Összes karakter állapotának mentése JSON-be** – a teljes `roster` lista mentése `savegame.json` fájlba (`GameRepository.saveRoster`).
9. **Összes karakter állapotának betöltése JSON-ből** – a teljes mentett névsor visszatöltése (`GameRepository.loadRoster`).

## 5\. Megvalósított Követelmények és Design Minta

### Design minta: Decorator (`EquipmentDecorator`)

* **Probléma:** A karakterek felszerelését futásidőben szeretnénk fegyver- és pajzs-módosítókkal (`FlameSwordDecorator`, `MagicShieldDecorator`) bővíteni anélkül, hogy minden kombinációhoz külön karakter-alosztályt kellene létrehozni.
* **Résztvevő osztályok:**

  * `EquipmentDecorator` (absztrakt ősosztály — nem implementál interfészt, saját `wrappedEquipment` referenciával láncolható)
  * `FlameSwordDecorator`, `MagicShieldDecorator` (konkrét dekorátor leszármazottak)
  * `Character` (kompozíción keresztül tartalmazza az aktuális `EquipmentDecorator` láncot, és ezen keresztül számítja a támadó- és védekező módosítókat)
* **Előny:** Tiszta felelősségi körök: a dekorátorok nem implementálnak semmilyen interfészt, mégis tetszőleges mélységben egymásba ágyazhatók.

### OOP követelmények

* **Encapsulation:** Privát mezők, konstruktorbeli és metódusszintű validáció, kontrollált lekérdező és állapotmódosító metódusok.
* **Abstraction, Inheritance \& Polymorphism:** `Fighter` interfész, `Character` és `EquipmentDecorator` absztrakt ősosztályok, valamint öt különböző kaszt saját `attack()`, `takeDamage()` vagy `heal()` viselkedéssel.
* **Collections \& Generics:** `List<Fighter>` a névsorhoz és generikus `Inventory<T extends Item>` osztály `Comparable<Item>` (érték szerint) és `Comparator` (súly szerint) rendezéssel.
* **Exceptions:** Egyedi `InvalidActionException` (`hu.rpg.service`) érvénytelen harci műveletekre.
* **Persistence:** Polimorf JSON szerializáció és deszerializáció Gson segítségével, `try-with-resources` blokkal a `GameRepository` osztályban (`saveRoster` / `loadRoster`).

## 6\. Közreműködők

* Projektkészítő: *Küzdő Bernát IHB9KK*

