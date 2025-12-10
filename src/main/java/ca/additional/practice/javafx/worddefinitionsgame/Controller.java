package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Map;

public class Controller {


    @FXML
    private ImageView Image;

    @FXML
    private Label d1;

    @FXML
    private Label d1t;

    @FXML
    private Label d2;

    @FXML
    private Label d2t;

    @FXML
    private Label d3;

    @FXML
    private Label d3t;

    @FXML
    private AnchorPane layout;

    @FXML
    private Label word;

    @FXML
    private Label wordType;

    @FXML
    void NextWord(ActionEvent event) {

    }

    //End of FXML variables

    //[Word, Definition] - For IO implementation
    private ArrayList<Map<String, String>> definitions;

    //Creating class instances
    private Model model;
    private WordAPI wordAPI;
    private DictionairyAPI dictionairyAPI;

    //-------- Start of controller;
    @FXML
    private void initialize() {
        wordAPI = new WordAPI();
        dictionairyAPI = new DictionairyAPI();
        model = new Model();
    }

}
