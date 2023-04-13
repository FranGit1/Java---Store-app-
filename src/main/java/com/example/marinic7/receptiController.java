package com.example.marinic7;

import files.FilesUtils;
import hr.java.production.model.Category;
import hr.java.production.model.Lijek;
import hr.java.production.model.Recept;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class receptiController {
    @FXML
    private TextField napomena;

    @FXML
    private Label redniBroj;


    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private CheckBox checkBox;


    @FXML
    private TableView<Recept> recptiTableView;

    @FXML
    private TableColumn<Recept,Integer> indexColumn;

    @FXML
    private TableColumn<Recept, Lijek> lijekColumn;
    @FXML
    private TableColumn<Recept, String> napomenaColumn;

    @FXML
    private TableColumn<Recept, LocalDateTime> timeColumn;

    @FXML
    private TableColumn<Recept, String> dostavColumn;



    private static List<Lijek> lijekovi = new ArrayList<>();



    private static List<Recept> recepti = new ArrayList<>();


    @FXML
    public void initialize() {
        recepti = FilesUtils.fetchReceptfromFile().get();
        System.out.println(recepti);
        lijekovi.add(new Lijek("Aspirin"));
        lijekovi.add(new Lijek("Andol C"));



        for(Lijek lijek : lijekovi){
            choiceBox.getItems().add(lijek.getName());

        }
        choiceBox.getItems().add("--Odaberite--");
        choiceBox.setValue("--Odaberite--");


    }

    @FXML
    public void saveItem() {

    }




}
