package hu.rpg.domain;

import java.util.Objects;

public class Item implements Comparable<Item> {
    private final String name;
    private final double weight;
    private final int value;

    public Item(String name, double weight, int value) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A tárgy neve nem lehet üres.");
        }
        if (weight < 0 || value < 0) {
            throw new IllegalArgumentException("A súly és az érték nem lehet negatív.");
        }
        this.name = name;
        this.weight = weight;
        this.value = value;
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

    @Override
    public int compareTo(Item other) {
        return Integer.compare(other.value, this.value);
    }

    @Override
    public String toString() {
        return String.format("%s (Súly: %.1f kg, Érték: %d arany)", name, weight, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Double.compare(item.weight, weight) == 0 && value == item.value && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, value);
    }
}
