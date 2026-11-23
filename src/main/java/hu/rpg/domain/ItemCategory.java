package hu.rpg.domain;

public enum ItemCategory {
    WEAPON("Fegyver"),
    DEFENSE("Védelem"),
    TREASURE("Érték");

    private final String displayName;

    ItemCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
