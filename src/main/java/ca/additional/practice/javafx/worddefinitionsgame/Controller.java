package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    //region FXML variables
    @FXML
    private AnchorPane root;

    @FXML
    private Label d1, d2, d3, wordLabel, scoreLabel;

    @FXML
    private Pane nounPane, verbPane, adjectivePane, adverbPane, prepositionPane, conjunctionPane, otherPane;

    private ArrayList<Pane> panes = new ArrayList<>();


    @FXML
    private ArrayList<Label> definitionsLabels = new ArrayList<>();


    @FXML
    private void NextWord(ActionEvent event) {

    }

    //endregion

    private double[] currentPosition = new double[2];
    private double[] orginalPosition = new double[2];

    private int score = 0;
    private Map<String, Integer> points = new HashMap<>();
    private int[] AnswerLog = new int[2];

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

        //Adding the points to the map
        points.put("correct", 100);
        points.put("wrong", -50);
        points.put("bonus", 1);

        points.put("unanswered", 0);
        points.put("skipped", 0);


        // Sets the word
        String word = wordAPI.getWord();
        m.setWord(word);

        //Sets the definitions
        try {
             Map<String, ArrayList<String>> definitions = dictionairyAPI.getDefinitions(m.getWord());
             m.setDefinitions(definitions);
             //m.setWord(dictionairyAPI.getCurrentWord()); // Update word in case it changed due to retry

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

        //Getting all panes within
        for ( Node node : root.getChildren())
            if (node instanceof Pane)
                panes.add((Pane) node);

        game();
    }

    // Game functions
   public void game(){
       System.out.println("Game");
        for(Label l : definitionsLabels){
            l.setText(m.getDefinition());
            l.setOnMousePressed(this::setMousePressed);
            l.setOnMouseDragged(this::setMouseDragged);
            l.setOnMouseReleased(this::setMouseReleased);
            System.out.println("Label added");
        }
   }

   //region Mouse Events
   private void setMousePressed(MouseEvent event){
       System.out.println("Pressed");
        Label l = (Label) event.getSource();
   }

   private void setMouseDragged(MouseEvent event){
       Label l = (Label) event.getSource();

       currentPosition[0] = event.getSceneX();
       currentPosition[1] = event.getSceneY();

       l.setLayoutX(currentPosition[0]);
       l.setLayoutY(currentPosition[1]);
   }

   private void setMouseReleased(MouseEvent event){
       Label l = (Label) event.getSource();

       if (isWithinPane(event, nounPane))
       {
           if (isNoun(l.getText())) isCorrect(l);
           else isIncorrect(l);

       }
       else if (isWithinPane(event, verbPane))
       {
           if (isVerb(l.getText())) isCorrect(l);
           else isIncorrect(l);
       }
       else if (isWithinPane(event, adjectivePane)){
           if (isAdjective(l.getText())) isCorrect(l);
           else isIncorrect(l);
       }
       else if (isWithinPane(event, adverbPane)){
           if (isAdverb(l.getText())) isCorrect(l);
           else isIncorrect(l);
       }
       else if (isWithinPane(event, prepositionPane)){
           if (isPreposition(l.getText())) isCorrect(l);
           else isIncorrect(l);
       }
       else if (isWithinPane(event, conjunctionPane)){
           if (isConjunction(l.getText())) isCorrect(l);
           else isIncorrect(l);
       }
       else if (isWithinPane(event, otherPane)){
           isSkipped(l);
       }
       else {
           l.setLayoutX(orginalPosition[0]);
           l.setLayoutY(orginalPosition[1]);
       }
   }
   //endregion

   //region Released helpers
   private void isCorrect(Label l){
       root.getChildren().remove(l);
       score += points.get("correct");
       scoreLabel.setText("Score: " + score);
   }

   private void isIncorrect(Label l){
       score -= points.get("wrong");
       scoreLabel.setText("Score: " + score);

       //relocate the definitions to their original location
       l.setLayoutX(orginalPosition[0]);
       l.setLayoutY(orginalPosition[1]);
   }

   private void isSkipped(Label l){
        root.getChildren().remove(l);
   }

   private boolean isWithinPane(MouseEvent event, Pane pane){
       double x = event.getSceneX();
       double y = event.getSceneY();

       return (x >= pane.getLayoutX() && x<= pane.getLayoutX() + pane.getWidth()
               &&
               y >= pane.getLayoutY() && y<= pane.getLayoutY() + pane.getHeight()
       );
   }


    //region 'is' Validation helpers functions
    private boolean isNoun(String definition) {
        ArrayList<String> nouns = m.getAllDefinitions().get("noun");
        return nouns != null && nouns.contains(definition);
    }

    private boolean isVerb(String definition) {
        ArrayList<String> verb = m.getAllDefinitions().get("verb");
        return verb != null && verb.contains(definition);
    }

    private boolean isAdjective(String definition) {
        ArrayList<String> adjective = m.getAllDefinitions().get("adjective");
        return adjective != null && adjective.contains(definition);
    }

    private boolean isAdverb(String definition) {
        ArrayList<String> adverb = m.getAllDefinitions().get("adverb");
        return adverb != null && adverb.contains(definition);
    }

    private boolean isPreposition(String definition) {
        ArrayList<String> preposition = m.getAllDefinitions().get("preposition");
        return preposition != null && preposition.contains(definition);
    }

    private boolean isConjunction(String definition) {
        ArrayList<String> conjunction = m.getAllDefinitions().get("conjunction");
        return conjunction != null && conjunction.contains(definition);
    }

    private boolean isOther(String definition) {
        return false;
    }

    //endregion
}
