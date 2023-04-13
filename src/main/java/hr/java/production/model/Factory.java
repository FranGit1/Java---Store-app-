package hr.java.production.model;

import java.io.Serializable;
import java.util.*;

/**
 * Class Factory that extends calss NamedEntity
 */
public class Factory extends NamedEntity implements Serializable {
    private Address address;
    private Set<Item> items = new HashSet<>();
    private String itemsStirng= "";


    /**
     * Constructor for class Factory with all of the avaliable fields
     * @param name
     * @param address
     * @param items
     */
    public Factory(String name, Address address, List<Item> items, Long id) {
        super(name,id);
        this.address = address;
        for(Item item:items){
            this.items.add(item);
                itemsStirng += item.getName();
                itemsStirng += " ";
        }
    }


    public void getAddress() {
        this.address.getGrad();
    }

    public String getItemsIds(){
        StringBuilder ids = new StringBuilder();
        Integer i=0;
        for(Item item: this.items){
            i++;
            if(i==this.items.size()){
                ids.append(item.getId());

            }else{
                ids.append(item.getId() + ",");

            }
        }


        return String.valueOf(ids);
    }

    public Address getAddressObj(){
        return this.address;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public String getItemsStirng() {
        return itemsStirng;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(!(obj instanceof Factory)){
            return false;
        }

        Factory i = (Factory) obj;
        return (i.address.equals(this.address)) && (i.items.equals(this.items))
                && super.equals(obj);

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),address, items);
    }

    @Override
    public String toString() {
        return "Factory{" +
                "address=" + address +
                ", items=" + items +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
