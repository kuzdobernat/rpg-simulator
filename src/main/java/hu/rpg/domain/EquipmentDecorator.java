package hu.rpg.domain;

public abstract class EquipmentDecorator {
    protected final EquipmentDecorator wrappedEquipment;

    protected EquipmentDecorator() {
        this.wrappedEquipment = null;
    }

    protected EquipmentDecorator(EquipmentDecorator wrappedEquipment) {
        this.wrappedEquipment = wrappedEquipment;
    }

    public EquipmentDecorator getWrappedEquipment() {
        return wrappedEquipment;
    }

    public int countInstancesOf(Class<? extends EquipmentDecorator> decoratorClass) {
        int current = decoratorClass.isInstance(this) ? 1 : 0;
        return current + (wrappedEquipment != null ? wrappedEquipment.countInstancesOf(decoratorClass) : 0);
    }

    public String getDescription() {
        return wrappedEquipment != null ? wrappedEquipment.getDescription() : "";
    }

    public int modifyOutgoingDamage(int baseDamage) {
        return wrappedEquipment != null ? wrappedEquipment.modifyOutgoingDamage(baseDamage) : baseDamage;
    }

    public int modifyIncomingDamage(int incomingDamage) {
        return wrappedEquipment != null ? wrappedEquipment.modifyIncomingDamage(incomingDamage) : incomingDamage;
    }
}
