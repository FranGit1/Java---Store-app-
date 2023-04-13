package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.enumeration.Gradovi;
import hr.java.production.model.*;
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

public class SearchStoreController {




        @FXML
        private TextField nameTextField;

        @FXML
        private TextField webAddressField;


        @FXML
        private ChoiceBox choiceBox;

        @FXML
        private TableView<Store> storesTableView;

        @FXML
        private TableColumn<Store,String> nameColumn;


        @FXML
        private TableColumn<Store,String> webAddressColumn;

        @FXML
        private TableColumn<Store,String> itemsColumn;


        private static List<Store> stores = new ArrayList<>();
        private static List<Category> categories = new ArrayList<>();
        private static List<Item> items = new ArrayList<>();



        @FXML
        public void initialize() {




           categories = FilesUtils.fetchCategoriesFromFile().get();


           items = FilesUtils.fetchItemsFromFile(categories).get();

           stores = FilesUtils.fetchStoresFromFile(items).get();




            nameColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });


            webAddressColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getWebAddress());
            });


            itemsColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getItemsStirng());
            });


            for(Store store : stores){
                for(Item it : store.getItems()){
                    choiceBox.getItems().add(it.getName());

                }
            }
            choiceBox.getItems().add("Show all");



            ObservableList<Store> storesObservableList = FXCollections.observableList(stores);
            storesTableView.setItems(storesObservableList);



        }



        @FXML
        protected void onSearchButtonClick(){
            String enteredName= nameTextField.getText();
            String enteredWebAddress =webAddressField.getText();

            if(!nameTextField.getText().isEmpty()){
                List<Store> filteredList = stores.stream()
                        .filter(s->s.getName().toLowerCase().contains(enteredName.toLowerCase()))
                        .collect(Collectors.toList());
                storesTableView.setItems(FXCollections.observableList(filteredList));
            }
            else if (!webAddressField.getText().isEmpty()) {
                List<Store> filteredList = stores.stream()
                        .filter(s -> s.getWebAddress().toLowerCase().contains(enteredWebAddress.toLowerCase()))
                        .collect(Collectors.toList());
                storesTableView.setItems(FXCollections.observableList(filteredList));

            }else if(!choiceBox.getSelectionModel().isEmpty()){

                if(((String) choiceBox.getValue()).toLowerCase().contains("all")){


                    storesTableView.setItems(FXCollections.observableList(stores));
                }else {


                    String selectedItem = (String) choiceBox.getValue();
                    List<Store> filteredList = stores.stream()
                            .filter(s -> s.getItemsStirng().toLowerCase().contains(selectedItem.toLowerCase()))
                            .collect(Collectors.toList());
                    storesTableView.setItems(FXCollections.observableList(filteredList));
                }
            }


        }











}
