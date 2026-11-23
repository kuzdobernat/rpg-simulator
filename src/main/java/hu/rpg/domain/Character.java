package hu.rpg.domain;

import hu.rpg.service.InvalidActionException;

public abstract class Character implements Fighter {
    private final String name;
    private int hp;
    private final int maxHp;
    private int mana;
    private final int baseAttack;
    private final Inventory<Item> inventory;
    private EquipmentDecorator equipment;

    protected Character(String name, int maxHp, int mana, int baseAttack) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A karakter neve nem lehet üres.");
        }
        if (maxHp <= 0 || baseAttack < 0 || mana < 0) {
            throw new IllegalArgumentException("Érvénytelen karakter statisztikák.");
        }
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.mana = mana;
        this.baseAttack = baseAttack;
        this.inventory = new Inventory<>(50.0);
        this.equipment = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    public int getMana() {
        return mana;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public EquipmentDecorator getEquipment() {
        return equipment;
    }

    public void equip(EquipmentDecorator equipment) {
        this.equipment = equipment;
    }

    protected int calculateTotalAttackDamage(int rawDamage) {
        return equipment != null ? equipment.modifyOutgoingDamage(rawDamage) : rawDamage;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            return;
        }
        int effectiveDamage = equipment != null ? equipment.modifyIncomingDamage(damage) : damage;
        this.hp = Math.max(0, this.hp - effectiveDamage);
    }

    @Override
    public void heal(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    protected void consumeMana(int amount) throws InvalidActionException {
        if (this.mana < amount) {
            throw new InvalidActionException(name + " nem rendelkezik elegendő manával (" + mana + "/" + amount + ")!");
        }
        this.mana -= amount;
    }

    protected void validateCanAct(Fighter target) throws InvalidActionException {
        if (!this.isAlive()) {
            throw new InvalidActionException(this.name + " már halott, nem tud támadni!");
        }
        if (target == null || !target.isAlive()) {
            throw new InvalidActionException("A célpont érvénytelen vagy már legyőzték!");
        }
        if (this == target) {
            throw new InvalidActionException("Egy karakter nem támadhatja meg saját magát!");
        }
    }

    @Override
    public String toString() {
        String eqDesc = (equipment != null && !equipment.getDescription().isEmpty())
                ? equipment.getDescription()
                : "Nincs";
        return String.format("%s [%s] - HP: %d/%d, Mana: %d, Alapsebzés: %d | Felszerelés: %s",
                name, getClass().getSimpleName(), hp, maxHp, mana, baseAttack, eqDesc);
    }
}
