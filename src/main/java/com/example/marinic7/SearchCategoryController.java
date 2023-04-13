package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchCategoryController {

    private static final int NUMBER_OF_LINES_FOR_CATEGORY = 3;
    private static final String FILE_NAME_CATEGORIES = "dat/categories.txt";

    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category,String> nameColumn;

    @FXML
    private TableColumn<Category,String> descirptionColummn;

    @FXML
    private TableColumn<Category,String> idColumn;

    private static List<Category> categories = new ArrayList<>();

    @FXML
    public void initialize() {
        categories = FilesUtils.fetchCategoriesFromFile().get();

        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        descirptionColummn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

        idColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });
        ObservableList<Category>categoriesObservableList = FXCollections.observableList(categories);
        categoriesTableView.setItems(categoriesObservableList);

    }

    @FXML
    protected void onSearchButtonClick(){
        String enteredName = nameTextField.getText();

            List<Category> filteredList = categories.stream()
                    .filter(s->s.getName().toLowerCase().contains(enteredName.toLowerCase()))
                    .collect(Collectors.toList());

            categoriesTableView.setItems(FXCollections.observableList(filteredList));




    }







}
