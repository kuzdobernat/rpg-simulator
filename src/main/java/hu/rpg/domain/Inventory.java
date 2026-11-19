package hu.rpg.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Inventory<T extends Item> {
    private final List<T> items;
    private final double maxWeight;

    public Inventory(double maxWeight) {
        if (maxWeight <= 0) {
            throw new IllegalArgumentException("A maximális teherbírásnak pozitívnak kell lennie.");
        }
        this.maxWeight = maxWeight;
        this.items = new ArrayList<>();
    }

    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Nem adható hozzá null tárgy.");
        }
        if (getCurrentWeight() + item.getWeight() > maxWeight) {
            throw new IllegalStateException("Túl nehéz! Nincs elég hely a hátizsákban: " + item.getName());
        }
        items.add(item);
    }

    public double getCurrentWeight() {
        return items.stream().mapToDouble(Item::getWeight).sum();
    }

    public List<T> getItemsSortedByValue() {
        List<T> copy = new ArrayList<>(items);
        Collections.sort(copy);
        return copy;
    }

    public List<T> getItemsSortedByWeight() {
        List<T> copy = new ArrayList<>(items);
        copy.sort(Comparator.comparingDouble(Item::getWeight));
        return copy;
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }
}
