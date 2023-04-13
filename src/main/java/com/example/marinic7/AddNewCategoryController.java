package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.GestureEvent;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddNewCategoryController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;
    private static List<Category> categories = new ArrayList<>();

    @FXML
    public void initialize() {
        categories = FilesUtils.fetchCategoriesFromFile().get();

    }


    @FXML
    public void saveCategory() {

        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();

        if(name.isEmpty()) {
            errorMessages.append("Name should not be empty!\n");
        }

        String description =  descriptionTextField.getText();

        if(description.isEmpty()) {
            errorMessages.append("description should not be empty!\n");
        }


        Long id = categories.get(categories.size()-1).getId()+1;



        if(errorMessages.isEmpty()) {
           Category newCategory = new Category(name,description,id);

            List<Category> categoryList = null;
            try {
                categoryList = FilesUtils.fetchCategoriesFromFile().get();

                categoryList.add(newCategory);

                FilesUtils.saveCategory(categoryList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeded!");
            alert.setHeaderText("Category data saved!");
            alert.setContentText("Category " + name +" saved to the database!");

            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Category data not saved!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }

}
