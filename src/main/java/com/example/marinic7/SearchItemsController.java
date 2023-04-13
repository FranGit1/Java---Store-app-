package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.*;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import hr.java.production.main.Main;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


public class SearchItemsController {
    private static final int NUMBER_OF_LINES_FOR_CATEGORY = 3;
    private static final String FILE_NAME_CATEGORIES = "dat/categories.txt";
    private static final String FILE_NAME_ITEMS = "dat/items.txt";


    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<Item> itemsTableView;

    @FXML
    private TableColumn<Item,String> nameColumn;

    @FXML
    private ChoiceBox choiceBox;


    @FXML
    private TableColumn<Item,String> categoryColumn;

    @FXML
    private TableColumn<Item,String> widthColumn;

    @FXML
    private TableColumn<Item,String> heightColumn;

    @FXML
    private TableColumn<Item,String> lengthColumn;

    @FXML
    private TableColumn<Item,String> productionCostsColumn;

    @FXML
    private TableColumn<Item,String> sellingPriceColumn;

    private static List<Item> items = new ArrayList<>();
    private static List<Category> categories = new ArrayList<>();

    @FXML
    public void initialize(){



        categories = FilesUtils.fetchCategoriesFromFile().get();

        items = FilesUtils.fetchItemsFromFile(categories).get();


        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        categoryColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCategory().getName());
        });

        widthColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getWidth().toString());
        });

        heightColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getHeight().toString());
        });

        lengthColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLength().toString());
        });

        productionCostsColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getProductionCost().toString());
        });

        sellingPriceColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSellingPrice().toString());
        });

        for(Category cat : categories) {
            choiceBox.getItems().add(cat.getName());
        }
        choiceBox.getItems().add("Show all items");

        ObservableList<Item> itemsObservableList = FXCollections.observableList(items);
        itemsTableView.setItems(itemsObservableList);
    }


    @FXML
    protected void onSearchButtonClick(){
        String enteredName = nameTextField.getText();
        if( !choiceBox.getSelectionModel().isEmpty()){

            if(((String) choiceBox.getValue()).toLowerCase().contains("all")){


                itemsTableView.setItems(FXCollections.observableList(items));
            }
            else {
                String selectedName = (String) choiceBox.getValue();
                List<Item> filteredList = items.stream()
                        .filter(s -> s.getCategory().getName().toLowerCase().contains(selectedName.toLowerCase()))
                        .collect(Collectors.toList());

                itemsTableView.setItems(FXCollections.observableList(filteredList));
            }
        }
        else{
            List<Item> filteredList = items.stream()
                    .filter(s->s.getName().toLowerCase().contains(enteredName.toLowerCase()))
                    .collect(Collectors.toList());

            itemsTableView.setItems(FXCollections.observableList(filteredList));
        }



    }






}
