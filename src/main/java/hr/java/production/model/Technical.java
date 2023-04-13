package hr.java.production.model;

/**
 * Sealed iterface Technical that can be implemented only by class Laptop
 */
public sealed interface Technical permits Laptop {
    public Integer getWaranty();
}
