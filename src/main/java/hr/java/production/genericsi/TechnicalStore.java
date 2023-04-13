package hr.java.production.genericsi;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.model.Technical;

import java.util.ArrayList;
import java.util.List;

public class TechnicalStore<T extends Technical> extends Store  {
    private List<T> elements;

    public TechnicalStore(String name, String webAddress,List<Item> items, List<T> l1) {
        super(name, webAddress, items,items.get(0).getId());
        elements = l1;

    }



    public List<T> getElements() {
        return elements;
    }
}

