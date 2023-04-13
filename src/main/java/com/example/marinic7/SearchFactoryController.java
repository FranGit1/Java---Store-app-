package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.enumeration.Gradovi;
import hr.java.production.model.*;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchFactoryController {



    @FXML
    private TextField nameTextField;

    @FXML
    private TextField cityTextField;


    @FXML
    private TextField postalTextField;


    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private TableView<Factory> factoriesTableView;

    @FXML
    private TableColumn<Factory,String> nameColumn;

    @FXML
    private TableColumn<Factory,String> cityColumn;

    @FXML
    private TableColumn<Factory,String> postalCodeColumn;

    @FXML
    private TableColumn<Factory,String> itemsColumn;


    private static List<Factory> factories = new ArrayList<>();
    private static List<Category> categories = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static List<Address> addresses = new ArrayList<>();



    @FXML
    public void initialize() {




      categories = FilesUtils.fetchCategoriesFromFile().get();

      items = FilesUtils.fetchItemsFromFile(categories).get();


       addresses = FilesUtils.fetchAddressFromFile().get();

       factories=FilesUtils.fetchFactoriesFromFile(addresses,items).get();




        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });


        cityColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddressObj().getCity());
        });

        postalCodeColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddressObj().getPostalCode().toString());
        });



        itemsColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getItemsStirng());
        });


        for(Factory fact : factories){
            for(Item it : fact.getItems()){
                choiceBox.getItems().add(it.getName());

            }
        }

        choiceBox.getItems().add("Show all");


        ObservableList<Factory> factoriesObservableList = FXCollections.observableList(factories);
       factoriesTableView.setItems(factoriesObservableList);



    }



    @FXML
    protected void onSearchButtonClick(){
        String enteredName= nameTextField.getText();

      String enteredCity =cityTextField.getText();
        String enteredPostalCode =postalTextField.getText();

        if(!nameTextField.getText().isEmpty()){
            List<Factory> filteredList = factories.stream()
                    .filter(s->s.getName().toLowerCase().contains(enteredName.toLowerCase()))
                    .collect(Collectors.toList());
           factoriesTableView.setItems(FXCollections.observableList(filteredList));
        }else if (!cityTextField.getText().isEmpty()){
            List<Factory> filteredList = factories.stream()
                    .filter(s->s.getAddressObj().getCity().toLowerCase().contains(enteredCity.toLowerCase()))
                    .collect(Collectors.toList());
            factoriesTableView.setItems(FXCollections.observableList(filteredList));

        }else if (!postalTextField.getText().isEmpty()){
            List<Factory> filteredList = factories.stream()
                    .filter(s->s.getAddressObj().getPostalCode().toString().toLowerCase().contains(enteredPostalCode.toLowerCase()))
                    .collect(Collectors.toList());
            factoriesTableView.setItems(FXCollections.observableList(filteredList));

        }else if(!choiceBox.getSelectionModel().isEmpty()){

            if(((String) choiceBox.getValue()).toLowerCase().contains("all")){


                factoriesTableView.setItems(FXCollections.observableList(factories));
            }else {


                String selectedItem = (String) choiceBox.getValue();
                List<Factory> filteredList = factories.stream()
                        .filter(s -> s.getItemsStirng().toLowerCase().contains(selectedItem.toLowerCase()))
                        .collect(Collectors.toList());
                factoriesTableView.setItems(FXCollections.observableList(filteredList));
            }
        }








    }






}



