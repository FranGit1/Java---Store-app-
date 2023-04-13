package hr.java.production.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Class Rice which extends  base class Item and implements interface Edible
 */
public class Rice extends Item  implements Edible{
    private final Integer CALORIES_PER_KG;
    private BigDecimal weight;

    /**
     * Constructor for class Rice with all of the avaliable fields
     * @param weight
     * @param caloriesPerKg
     * @param name
     * @param category
     * @param width
     * @param height
     * @param length
     * @param productionCost
     * @param sellingPrice
     * @param discount
     */
    public Rice(BigDecimal weight,Integer caloriesPerKg,String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice,BigDecimal discount,Long id) {
        super( name,  category,  width,  height,  length,  productionCost,  sellingPrice,discount,id);
        this.CALORIES_PER_KG = caloriesPerKg;
        this.weight = weight;

    }


    /**
     * Used to calculate number of kilocalories for item
     * @return Returns number of kilocalories of item as integer number
     */

    public Integer calculateKilocalories(){
        return this.CALORIES_PER_KG;

    }

    /**
     * Used to calculate price of item
     * @return Returns price of item with discount applied in bigdecimal
     */
    public BigDecimal calculatePrice(){
        BigDecimal originalPrice =this.weight.multiply(super.getSellingPrice());
        BigDecimal percentage =super.getDiscount().discountAmount();
        BigDecimal divisor = new BigDecimal("100.0");
        BigDecimal popust =originalPrice.multiply(percentage.divide(divisor));
        return(originalPrice.subtract(popust));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rice rice = (Rice) o;
        return Objects.equals(CALORIES_PER_KG, rice.CALORIES_PER_KG) && Objects.equals(weight, rice.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), CALORIES_PER_KG, weight);
    }
}
