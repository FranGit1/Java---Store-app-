package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AddNewStoreController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField webAddressTextField;

    @FXML
    private TextField idsTextField;

    private static List<Category> categories = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static List<Store> stores = new ArrayList<>();

    @FXML
    public void initialize() {

        categories = FilesUtils.fetchCategoriesFromFile().get();
        items = FilesUtils.fetchItemsFromFile(categories).get();
        stores = FilesUtils.fetchStoresFromFile(items).get();
    }

    @FXML
    public void saveStore() {
        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();

        if(name.isEmpty()) {
            errorMessages.append("Name should not be empty!\n");
        }

        String webAddress = webAddressTextField.getText();

        if(webAddress.isEmpty()) {
            errorMessages.append("webAddress should not be empty!\n");
        }



        String ids =idsTextField.getText();

        if(ids.isEmpty()) {
            errorMessages.append("ids should not be empty!\n");
        }

        Long id = stores.get(stores.size()-1).getId()+1;

        List<Long> idsList = List.of(ids.split(",")).stream().map(s -> Long.parseLong(s)).toList();
        List<Item> itemsForStore = new ArrayList<>();

        for (Item item : items) {
            for (Long ln : idsList) {
                if (Objects.equals(ln, item.getId())) {
                    itemsForStore.add(item);
                }
            }
        }

        if(errorMessages.isEmpty()) {
            Store newStore = new Store(name,webAddress,itemsForStore,id);

            List<Store> storeList = null;
            try {
                storeList = FilesUtils.fetchStoresFromFile(items).get();

                storeList.add(newStore);

                FilesUtils.saveStore(storeList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeded!");
            alert.setHeaderText("Store data saved!");
            alert.setContentText("Store " + name + " "+ id +" saved to the database!");

            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Store data not saved!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    }




}
