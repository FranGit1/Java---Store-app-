package hr.java.production.model;

import java.math.BigDecimal;

/**
 * Interface Edible that is implemented by every class of which objects can be eaten
 */
public interface Edible {
    /**
     * Used to calculate kilocalories for item
     * @return Integer number of kilocalories of item
     */
    public Integer calculateKilocalories();

    /**
     * Used to calculate price of item
     * @return BigDceimal number which represents price of item
     */
    public BigDecimal calculatePrice();
}
