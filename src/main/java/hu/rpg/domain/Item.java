package hu.rpg.domain;

import java.util.Objects;

public class Item implements Comparable<Item> {
    private final String name;
    private final double weight;
    private final int value;
    private final ItemCategory category;

    public Item(String name, double weight, int value, ItemCategory category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A tárgy neve nem lehet üres.");
        }
        if (weight < 0 || value < 0) {
            throw new IllegalArgumentException("A súly és az érték nem lehet negatív.");
        }
        if (category == null) {
            throw new IllegalArgumentException("A tárgy kategóriája nem lehet null.");
        }
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.category = category;
    }

    public Item(String name, double weight, int value) {
        this(name, weight, value, ItemCategory.TREASURE);
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public ItemCategory getCategory() {
        return category;
    }

    @Override
    public int compareTo(Item other) {
        return Integer.compare(other.value, this.value);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (Súly: %.1f kg, Érték: %d arany)",
                name, category.getDisplayName(), weight, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Double.compare(item.weight, weight) == 0
                && value == item.value
                && category == item.category
                && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, value, category);
    }
}
