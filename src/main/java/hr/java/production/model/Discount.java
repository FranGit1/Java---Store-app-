package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Record named Discount that stores info on amount of discount
 */
public record Discount(BigDecimal discountAmount ) implements Serializable {
    public Discount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(!(obj instanceof Discount)){
            return false;
        }

        Discount i = (Discount) obj;
        return (i.discountAmount.equals(this.discountAmount));
    }


    @Override
    public int hashCode() {
        return Objects.hash(discountAmount);
    }
}
