package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Map;

public class Controller {


    @FXML
    private ImageView Image;

    @FXML
    private Label d1;

    @FXML
    private Label d2;

    @FXML
    private Label d3;

    @FXML
    private ArrayList<Label> definitionsLabels = new ArrayList<>();

    @FXML
    private AnchorPane layout;

    @FXML
    private Label wordLabel, wordType;

    @FXML
    void NextWord(ActionEvent event) {

    }

    private double[] currentPosition = new double[2];
    private double[] orginalPosition = new double[2];

    //End of FXML variables

    //[Word, Definition] - For IO implementation

    //Creating class instances
    private Model m;
    private WordAPI wordAPI;
    private DictionairyAPI dictionairyAPI;

    //-------- Start of controller;
    @FXML
    public void initialize() {
        //Creating class instances
        wordAPI = new WordAPI();
        dictionairyAPI = new DictionairyAPI();
        m = new Model();

        //Adds the definitions labels to the array list
        definitionsLabels.add(d1);
        definitionsLabels.add(d2);
        definitionsLabels.add(d3);

        // Sets the word
        String word = wordAPI.getWord();
        m.setWord(word);

        //Sets the definitions
        try {
             Map<String, ArrayList<String>> definitions = dictionairyAPI.getDefinitions(m.getWord());
             m.setDefinitions(definitions);

             if (m.getAllDefinitions().isEmpty()) throw new IndexOutOfBoundsException();
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Failed to fetch definitions due to the lack of them");
        }
        catch (Exception e) {
            System.out.println("Failed to fetch definitions");
        }
        finally {
            d1.setText(m.getDefinition());
            d2.setText(m.getDefinition());
            d3.setText(m.getDefinition());

            wordLabel.setText(m.getWord());
        }
    }

    // HELPER FUNCTIONS
   public void game(){
        for(Label l : definitionsLabels){
            l.setText(m.getDefinition());
            l.setOnMousePressed(this::setMousePressed);
            l.setOnMouseDragged(this::setMouseDragged);
            l.setOnMouseReleased(this::setMouseReleased);
        }
   }

   private void setMousePressed(MouseEvent event){
        Label l = (Label) event.getSource();

       orginalPosition[0] = l.getLayoutX();
       orginalPosition[1] = l.getLayoutY();
   }

   private void setMouseDragged(MouseEvent event){
       Label l = (Label) event.getSource();

       currentPosition[0] = event.getSceneX();
       orginalPosition[1] = event.getSceneY();
   }

   private void setMouseReleased(MouseEvent event){
       Label l = (Label) event.getSource();
   }
   
}
