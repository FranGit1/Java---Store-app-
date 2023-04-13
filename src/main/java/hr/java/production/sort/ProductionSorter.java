package hr.java.production.sort;

import hr.java.production.model.Item;

import java.util.Comparator;

public class ProductionSorter implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        if(o1.getSellingPrice().compareTo(o2.getSellingPrice())==1){
            return 1;
        }else if (o1.getSellingPrice().compareTo(o2.getSellingPrice())==-1){
            return -1;
        }else{
            return 0;

        }
    }
}
