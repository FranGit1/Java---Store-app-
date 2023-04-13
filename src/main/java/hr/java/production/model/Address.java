package hr.java.production.model;

import hr.java.production.enumeration.Gradovi;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class Address
 */
public class Address implements Serializable {
    private String street;
    private String houseNumber;
    private String city;
    private Integer postalCode;
    private Gradovi grad;

    /**
     * Class Builder used to initialize object of class Address with not every field of object Address
     */
    public static class Builder {
        private String street;
        private String houseNumber;
        private String city;
        private Integer postalCode;
        private Gradovi grad;


        public Builder(String street){
            this.street = street;
        }
        public Builder(){}

        public Builder asStreet(String street){
            this.street = street;
            return this;
        }


        public Builder asGrad(Gradovi grad){
            this.grad = grad;
            return this;
        }


        public Builder houseNumber(String houseNumber){
            this.houseNumber=houseNumber;
            return this;
        }

        public Builder atCity(String city){
            this.city=city;
            return this;
        }

        public Builder atPostal(Integer postal){
            this.postalCode=postal;
            return this;
        }

        public Address build(){
            Address address= new Address();
            address.street=this.street;
            address.houseNumber=this.houseNumber;
            address.city=this.city;
            address.postalCode=this.postalCode;
            address.grad=this.grad;
            return address;
        }

    }

    private Address(){
    }

    public String getCity() {
        return city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void getGrad(){
        System.out.println(this.grad.getIme() + " " + this.grad.getBroj());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(!(obj instanceof Address)){
            return false;
        }

        Address i = (Address) obj;
        return (i.street.equals(this.street)) && (i.houseNumber.equals(this.houseNumber)) && (i.city.equals(this.city)) && (i.postalCode.equals(this.postalCode));

    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber, city, postalCode);
    }


    @Override
    public String toString() {
        return this.city + " " + this.grad + " " + this.postalCode;
    }
}
