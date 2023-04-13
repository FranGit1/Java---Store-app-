package hr.java.production.model;

import java.math.BigDecimal;

/**
 * Class Laptop which extend base class Item and implements interface Technical
 */
public non-sealed class Laptop extends Item implements Technical{
    public Integer waranty;

    /**
     * Constructor for class Laptop with all of the avaliable fields
     * @param waranty
     * @param name
     * @param category
     * @param width
     * @param height
     * @param length
     * @param productionCost
     * @param sellingPrice
     * @param discount
     */
    public Laptop(Integer waranty, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice,BigDecimal discount,Long id) {
        super( name,  category,  width,  height,  length,  productionCost,  sellingPrice,discount,id);
        this.waranty = waranty;
    }

    public void setWaranty(Integer waranty) {
        waranty = waranty;
    }

    public Integer getWaranty(){
        return this.waranty;
    }
}
