package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddNewItemController {


    @FXML
    private TextField nameTextField;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private TextField productionCostTextField;

    @FXML
    private TextField sellingPriceTextField;

    @FXML
    private ChoiceBox choiceBox;

    private static List<Category> categories = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();


    @FXML
    public void initialize() {

        categories = FilesUtils.fetchCategoriesFromFile().get();
        items = FilesUtils.fetchItemsFromFile(categories).get();
        for(Category cat : categories) {
            choiceBox.getItems().add(cat.getName());
        }



    }

    @FXML
    public void saveItem() {

        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();

        if(name.isEmpty()) {
            errorMessages.append("Name should not be empty!\n");
        }

        String category =  (String) choiceBox.getValue();
        Optional<Category> cat1 = categories.stream().filter(s->s.getName().toLowerCase().equals(category.toLowerCase())).findAny();
//        if(category.isEmpty()) {
//            errorMessages.append("Category should not be empty!\n");
//        }else if(category.toLowerCase().equals("literature")){
//            cat1=categories.get(2);
//        }

        BigDecimal width = BigDecimal.ZERO;
        String widthString = widthTextField.getText();

        if(widthString.isEmpty()) {
            errorMessages.append("Width should not be empty!\n");
        }
        else {
            try {
                width = new BigDecimal(widthString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Width is not in valid format!\n");
            }
        }

        BigDecimal height = BigDecimal.ZERO;
        String heightString = heightTextField.getText();

        if(heightString.isEmpty()) {
            errorMessages.append("height should not be empty!\n");
        }
        else {
            try {
                height = new BigDecimal(heightString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("height is not in valid format!\n");
            }
        }
        Long id = items.get(items.size()-1).getId()+1;

        BigDecimal length = BigDecimal.ZERO;
        String lengthString = lengthTextField.getText();




        if(lengthString.isEmpty()) {
            errorMessages.append("length should not be empty!\n");
        }
        else {
            try {
                length = new BigDecimal(lengthString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("length is not in valid format!\n");
            }
        }

        BigDecimal productionCost = BigDecimal.ZERO;
        String productionCostString = productionCostTextField.getText();

        if(productionCostString.isEmpty()) {
            errorMessages.append("productionCost should not be empty!\n");
        }
        else {
            try {
                productionCost = new BigDecimal(productionCostString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("productionCost is not in valid format!\n");
            }
        }



        BigDecimal sellingPrice = BigDecimal.ZERO;
        String sellingPriceString = sellingPriceTextField.getText();

        if(sellingPriceString.isEmpty()) {
            errorMessages.append("sellingPrice should not be empty!\n");
        }
        else {
            try {
                sellingPrice = new BigDecimal(sellingPriceString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("sellingPrice is not in valid format!\n");
            }
        }

        if(errorMessages.isEmpty()) {
            Item newItem = new Item(name,cat1.get(),width,height,length,productionCost,sellingPrice,id);

            List<Item> ItemList = null;
            try {
                ItemList = FilesUtils.fetchItemsFromFile(categories).get();

                ItemList.add(newItem);

                FilesUtils.saveItem(ItemList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeded!");
            alert.setHeaderText("Item data saved!");
            alert.setContentText("Student " + name + " " + category.toString() + " saved to the database!");

            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Item data not saved!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }







    }





