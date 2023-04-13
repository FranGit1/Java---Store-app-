package hr.java.production.genericsi;

import hr.java.production.model.Edible;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.util.List;

public class FoodStore <T extends Edible> extends Store {
   private List<T> elements;
    public FoodStore(String name, String webAddress, List<Item> items,List<T> l1) {
        super(name, webAddress, items,items.get(0).getId());
        elements = l1;
    }

    public List<T> getElements() {
        return elements;
    }
}
