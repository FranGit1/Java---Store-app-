package hr.java.production.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class Store which extends class NamedEntity
 */
public class Store extends NamedEntity implements Serializable {
    private String webAddress;
    private Set<Item> items = new HashSet<>();
    private String itemsStirng= "";

    /**
     * Constructor for class Store with all of the avaliable fields
     * @param name
     * @param webAddress
     * @param items
     */
    public Store(String name, String webAddress, Item[] items,Long id) {
        super(name,id);
        this.webAddress = webAddress;
        for(Item item:items){
            this.items.add(item);

        }

    }


    public Store(String name, String webAddress, List<Item> items,Long id) {
        super(name,id);
        this.webAddress = webAddress;
        for(Item item:items){
            this.items.add(item);
            itemsStirng += item.getName();
            itemsStirng += " ";
        }

    }
    public String getWebAddress() {
        return webAddress;
    }

    public String getItemsStirng() {
        return itemsStirng;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    /**
     * Used to get names of all items stored in object of class Store
     */
    public void getItemsListed(){
        for(Item item:this.items){
            System.out.println(item.getName());
        }
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


    public void setName(String name) {
        this.name = name;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(!(obj instanceof Store)){
            return false;
        }

        Store i = (Store) obj;
        return (i.webAddress.equals(this.webAddress)) && (i.items.equals(this.items))
                && super.equals(obj);

    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),webAddress, items);
    }
}
