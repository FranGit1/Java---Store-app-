package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Base class Item that extend class NamedEntity
 */
public class Item extends NamedEntity implements Serializable  {
    private Category category;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal length;
    private BigDecimal productionCost;
    private BigDecimal sellingPrice;
    private  Discount discount;


    /**
     * Constructor for class Item with all of the avaliable fields
     * @param name
     * @param category
     * @param width
     * @param height
     * @param length
     * @param productionCost
     * @param sellingPrice
     * @param discount
     */
    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice,BigDecimal discount,Long id) {
        super(name,id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = new Discount(discount);
    }

    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice,Long id){
        super(name,id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }

    public Discount getDiscount() {
        return discount;
    }

    public Category getCategory() {
        return category;
    }

    public String getStringCategory(){
        return category.toString();
    }

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public BigDecimal getVolume(){
        return this.height.multiply(this.length).multiply(this.width);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(!(obj instanceof Item)){
            return false;
        }

        Item i = (Item) obj;
        return (i.category.equals(this.category)) && ((i.width.compareTo(this.width))==0) && ((i.height.compareTo(this.height))==0)
                && ((i.length.compareTo(this.length))==0) && ((i.productionCost.compareTo(this.productionCost))==0) && ((i.sellingPrice.compareTo(this.sellingPrice))==0)
                && (i.discount.equals(this.discount)) && super.equals(obj);

    }

    @Override
    public String toString(){
        return String.format( "Imena "+ super.name + " "+ this.id);
    }



    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),category, width, height, length, productionCost, sellingPrice, discount);
    }
}


