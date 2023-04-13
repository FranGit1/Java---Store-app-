package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AddNewFactoryController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private TextField idsTextField;

    private static List<Category> categories = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static List<Address> addresses = new ArrayList<>();
    private static List<Factory> factories = new ArrayList<>();


    @FXML
    public void initialize() {

        categories = FilesUtils.fetchCategoriesFromFile().get();
        items = FilesUtils.fetchItemsFromFile(categories).get();
       addresses=FilesUtils.fetchAddressFromFile().get();
        factories=FilesUtils.fetchFactoriesFromFile(addresses,items).get();
    }




    @FXML
    public void saveFactory() {
        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();

        if(name.isEmpty()) {
            errorMessages.append("Name should not be empty!\n");
        }

        String city = cityTextField.getText();

        if(city.isEmpty()) {
            errorMessages.append("city should not be empty!\n");
        }

        String postalCode = postalCodeTextField.getText();

        if(postalCode.isEmpty()) {
            errorMessages.append("postalCode should not be empty!\n");
        }else if(!(postalCode.equals("23000")) ||!(postalCode.equals("10000"))){
            errorMessages.append("address doesnt exist in database!\n");

        }



        String ids =idsTextField.getText();

        if(ids.isEmpty()) {
            errorMessages.append("ids should not be empty!\n");
        }

        Long id = factories.get(factories.size()-1).getId()+1;

        List<Long> idsList = List.of(ids.split(",")).stream().map(s -> Long.parseLong(s)).toList();
        List<Item> itemsForStore = new ArrayList<>();

        for (Item item : items) {
            for (Long ln : idsList) {
                if (Objects.equals(ln, item.getId())) {
                    itemsForStore.add(item);
                }
            }
        }
        Optional<Address> addressObj = addresses.stream().filter(s->s.getPostalCode()==Integer.parseInt(postalCode)).findAny();
        if(errorMessages.isEmpty()) {
            Factory newFactory = new Factory(name,addressObj.get(),itemsForStore,id);

            List<Factory> factoryList = null;
            try {
                factoryList = FilesUtils.fetchFactoriesFromFile(addresses,items).get();

                factoryList.add(newFactory);

                FilesUtils.saveFactory(factoryList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeded!");
            alert.setHeaderText("Factory data saved!");
            alert.setContentText("Factory " + name + " "+ id +" saved to the database!");

            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Factory data not saved!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    }








}
