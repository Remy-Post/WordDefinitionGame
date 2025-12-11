/*
 * File: Controller.java
 * Purpose: JavaFX Controller class that manages the game UI and handles user interactions.
 *          Implements drag-and-drop functionality for matching definitions to word types,
 *          tracks scores, and manages game flow.
 * 
 * Global Variables:
 *   - root: AnchorPane - Root pane of the UI
 *   - d1, d2, d3: Label - Labels displaying definitions that can be dragged
 *   - wordLabel: Label - Displays the current word
 *   - scoreLabel: Label - Displays the current score
 *   - labelOriginalPostion: Map<Label, double[]> - Stores original positions of definition labels
 *   - nounPane, verbPane, adjectivePane, adverbPane, prepositionPane, conjunctionPane, otherPane: Pane - Drop zones for each part of speech
 *   - panes: ArrayList<Pane> - List of all panes in the UI
 *   - definitionsLabels: ArrayList<Label> - List of definition labels
 *   - currentPosition: double[] - Current mouse position during drag
 *   - orginalPosition: double[] - Original position of label before drag
 *   - score: int - Current game score
 *   - points: Map<String, Integer> - Point values for different outcomes
 *   - log: Map<Integer, List<Integer>> - Game history log
 *   - AnswerLog: int[] - Log of answers
 *   - m: Model - Game data model
 *   - wordAPI, dictionairyAPI, wordsFromFile: API instances for data fetching
 * 
 * Major Classes/Functions:
 *   - initialize(): Initializes the controller and sets up the game
 *   - setup(): Sets up initial game state
 *   - game(): Main game loop that displays words and definitions
 *   - handleNewWord(): Loads a new word when Next button is clicked
 *   - Mouse event handlers: setMousePressed, setMouseDragged, setMouseReleased
 *   - Validation helpers: isNoun, isVerb, isAdjective, etc.
 *   - Response handlers: isCorrect, isIncorrect, isSkipped
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.*;

public class Controller {

    //region FXML variables
    @FXML
    private AnchorPane root;

    @FXML
    private Label d1, d2, d3, wordLabel, scoreLabel;

    @FXML
    private Map<Label, double[]> labelOriginalPostion = new HashMap<>();

    @FXML
    private Pane nounPane, verbPane, adjectivePane, adverbPane, prepositionPane, conjunctionPane, otherPane;

    private ArrayList<Pane> panes = new ArrayList<>();


    @FXML
    private ArrayList<Label> definitionsLabels = new ArrayList<>();


    /**
     * Event handler for the Next button to load a new word.
     * 
     * What it does: Calls handleNewWord() when the Next button is clicked.
     * 
     * Inputs:
     *   - event: ActionEvent - The button click event
     * 
     * Return value: void
     * 
     * Side effects: Loads a new word and updates the game state
     */
    @FXML
    private void NextWord(ActionEvent event) { handleNewWord(); }

    //endregion

    private double[] currentPosition = new double[2];
    private double[] orginalPosition = new double[2];

    private int score = 0;
    private Map<String, Integer> points = new HashMap<>();
    private Map<Integer, List<Integer>> log = new HashMap<>();
    private int[] AnswerLog = new int[2];

    //End of FXML variables

    //[Word, Definition] - For IO implementation

    //Creating class instances
    private Model m;
    private WordAPI wordAPI;
    private DictionairyAPI dictionairyAPI;
    private WordsFromFile wordsFromFile;

    //-------- Start of controller;
    /**
     * Initializes the controller after FXML elements are loaded.
     * 
     * What it does: Sets up all game components including API instances, UI elements,
     *               point values, and pane collections. Then calls setup() and game() to start.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects: Initializes all instance variables, populates collections, starts the game
     */
    @FXML
    public void initialize() {
        //region Variable iniatisation

        //Creating class instances
        wordAPI = new WordAPI();
        dictionairyAPI = new DictionairyAPI();
        m = new Model();
        wordsFromFile = new WordsFromFile();

        //Adds the definitions labels to the array list
        definitionsLabels.add(d1);
        definitionsLabels.add(d2);
        definitionsLabels.add(d3);
        labelOriginalPostion.put(d1, new double[] {d1.getLayoutX(), d1.getLayoutY()});
        labelOriginalPostion.put(d2, new double[] {d2.getLayoutX(), d2.getLayoutY()});
        labelOriginalPostion.put(d3, new double[] {d3.getLayoutX(), d3.getLayoutY()});

        //Adding the points to the map
        points.put("correct", 100);
        points.put("wrong", -50);
        points.put("bonus", 1);

        points.put("unanswered", 0);
        points.put("skipped", 0);


        //Getting all panes within
        for ( Node node : root.getChildren())
            if (node instanceof Pane)
                panes.add((Pane) node);

        //endregion

        setup();
        game();

    }

    /**
     * Sets up the initial game state.
     * 
     * What it does: Displays the current word in the word label.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects: Updates the wordLabel text
     */
    private void setup() {
        wordLabel.setText(m.getWord());
    }

    /**
     * Handles loading a new word when the Next button is clicked.
     * 
     * What it does: Creates a new Model instance to load a new word and definitions,
     *               then calls game() to update the UI.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects: Resets the model, updates UI with new word and definitions
     */
    public void handleNewWord(){
        m = new Model(); //Reset the data
        game(); //Run game

        System.out.println("Next word");
        System.out.println(definitionsLabels.size());
    }


    // Game functions
    /**
     * Main game method that updates the UI with current word and definitions.
     * 
     * What it does: Displays the current word, ensures enough definition labels exist,
     *               populates labels with definitions, and sets up mouse event handlers
     *               for drag-and-drop functionality.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects: Updates UI labels, creates new labels if needed, attaches event handlers
     */
   public void game(){

        wordLabel.setText(m.getWord());

       System.out.println("Game");
       if (definitionsLabels.size() < 3) {
           Label l = new Label();
           if (definitionsLabels.size() == 2) {
               l.setLayoutX(labelOriginalPostion.get(d3)[0]);
               l.setLayoutX(labelOriginalPostion.get(d3)[1]);
           }
           else if (definitionsLabels.size() == 1) {
               l.setLayoutX(labelOriginalPostion.get(d2)[0]);
               l.setLayoutX(labelOriginalPostion.get(d2)[1]);
           }
           else {
               l.setLayoutX(labelOriginalPostion.get(d1)[0]);
               l.setLayoutX(labelOriginalPostion.get(d1)[1]);
           }
           definitionsLabels.add(l);
       }

       ArrayList<String> definitionsArray = m.getAllDefinitionsAsList();
       int i = 0;
       try{
            for(Label l : definitionsLabels){
                if (definitionsArray.size() <= i) break;
                l.setText(definitionsArray.get(definitionsLabels.indexOf(l)));
                l.setOnMousePressed(this::setMousePressed);
                l.setOnMouseDragged(this::setMouseDragged);
                l.setOnMouseReleased(this::setMouseReleased);
                System.out.println("Label added");
            }
       }catch (Exception e){
           System.err.println("Error adding label");
       }
   }

   //region Mouse Events
   /**
    * Handles mouse press events on definition labels.
    * 
    * What it does: Records when a definition label is clicked to start a drag operation.
    * 
    * Inputs:
    *   - event: MouseEvent - The mouse press event
    * 
    * Return value: void
    * 
    * Side effects: Prints "Pressed" to console
    */
   private void setMousePressed(MouseEvent event){
       System.out.println("Pressed");
        Label l = (Label) event.getSource();
   }

   /**
    * Handles mouse drag events on definition labels.
    * 
    * What it does: Updates the label's position to follow the mouse cursor during dragging.
    * 
    * Inputs:
    *   - event: MouseEvent - The mouse drag event
    * 
    * Return value: void
    * 
    * Side effects: Updates label position, modifies currentPosition array
    */
   private void setMouseDragged(MouseEvent event){
       Label l = (Label) event.getSource();

       currentPosition[0] = event.getSceneX();
       currentPosition[1] = event.getSceneY();

       l.setLayoutX(currentPosition[0]);
       l.setLayoutY(currentPosition[1]);
   }

   /**
    * Handles mouse release events on definition labels.
    * 
    * What it does: Checks which pane (if any) the label was dropped on and validates
    *               if the definition matches that part of speech. Calls appropriate
    *               response handler (isCorrect, isIncorrect, isSkipped) based on result.
    * 
    * Inputs:
    *   - event: MouseEvent - The mouse release event
    * 
    * Return value: void
    * 
    * Side effects: Updates score, removes or repositions labels based on validation
    */
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
           l.setLayoutX(labelOriginalPostion.get(l)[0]);
           l.setLayoutY(labelOriginalPostion.get(l)[1]);
       }
   }
   //endregion

   //region Released helpers
   /**
    * Handles correct definition placement.
    * 
    * What it does: Removes the label from the UI, adds points to score, and updates score display.
    * 
    * Inputs:
    *   - l: Label - The definition label that was correctly placed
    * 
    * Return value: void
    * 
    * Side effects: Removes label from UI, increases score, updates scoreLabel
    */
   private void isCorrect(Label l){
       root.getChildren().remove(l);
       score += points.get("correct");
       scoreLabel.setText("Score: " + score);
   }

   /**
    * Handles incorrect definition placement.
    * 
    * What it does: Deducts points from score, updates score display, and returns the label
    *               to its original position.
    * 
    * Inputs:
    *   - l: Label - The definition label that was incorrectly placed
    * 
    * Return value: void
    * 
    * Side effects: Decreases score, updates scoreLabel, repositions label
    */
   private void isIncorrect(Label l){
       score += points.get("wrong"); //Value -50
       scoreLabel.setText("Score: " + score);

       //relocate the definitions to their original location
       l.setLayoutX(labelOriginalPostion.get(l)[0]);
       l.setLayoutY(labelOriginalPostion.get(l)[1]);
   }

   /**
    * Handles skipped definition (dropped on "Other" pane).
    * 
    * What it does: Removes the label from the UI without affecting the score.
    * 
    * Inputs:
    *   - l: Label - The definition label that was skipped
    * 
    * Return value: void
    * 
    * Side effects: Removes label from UI
    */
   private void isSkipped(Label l){
        root.getChildren().remove(l);
   }

   /**
    * Checks if the mouse cursor is within the bounds of a specific pane.
    * 
    * What it does: Determines if the current mouse position falls within the boundaries
    *               of the specified pane.
    * 
    * Inputs:
    *   - event: MouseEvent - The mouse event containing cursor position
    *   - pane: Pane - The pane to check bounds against
    * 
    * Return value: boolean - true if cursor is within pane bounds, false otherwise
    * 
    * Side effects: None
    */
   private boolean isWithinPane(MouseEvent event, Pane pane){
       double x = event.getSceneX();
       double y = event.getSceneY();

       return (x >= pane.getLayoutX() && x<= pane.getLayoutX() + pane.getWidth()
               &&
               y >= pane.getLayoutY() && y<= pane.getLayoutY() + pane.getHeight()
       );
   }

   //endregion


    //region 'is' Validation helpers functions
    private boolean isNoun(String definition) {
        ArrayList<String> nouns = m.getAllDefinitions().get("noun");
        return nouns != null && nouns.contains(definition);
    }

    /**
     * Validates if a definition is a verb.
     * 
     * What it does: Checks if the given definition exists in the model's verb definitions.
     * 
     * Inputs:
     *   - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a verb, false otherwise
     * 
     * Side effects: None
     */
    private boolean isVerb(String definition) {
        ArrayList<String> verb = m.getAllDefinitions().get("verb");
        return verb != null && verb.contains(definition);
    }

    /**
     * Validates if a definition is an adjective.
     * 
     * What it does: Checks if the given definition exists in the model's adjective definitions.
     * 
     * Inputs:
     *   - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is an adjective, false otherwise
     * 
     * Side effects: None
     */
    private boolean isAdjective(String definition) {
        ArrayList<String> adjective = m.getAllDefinitions().get("adjective");
        return adjective != null && adjective.contains(definition);
    }

    /**
     * Validates if a definition is an adverb.
     * 
     * What it does: Checks if the given definition exists in the model's adverb definitions.
     * 
     * Inputs:
     *   - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is an adverb, false otherwise
     * 
     * Side effects: None
     */
    private boolean isAdverb(String definition) {
        ArrayList<String> adverb = m.getAllDefinitions().get("adverb");
        return adverb != null && adverb.contains(definition);
    }

    /**
     * Validates if a definition is a preposition.
     * 
     * What it does: Checks if the given definition exists in the model's preposition definitions.
     * 
     * Inputs:
     *   - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a preposition, false otherwise
     * 
     * Side effects: None
     */
    private boolean isPreposition(String definition) {
        ArrayList<String> preposition = m.getAllDefinitions().get("preposition");
        return preposition != null && preposition.contains(definition);
    }

    /**
     * Validates if a definition is a conjunction.
     * 
     * What it does: Checks if the given definition exists in the model's conjunction definitions.
     * 
     * Inputs:
     *   - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a conjunction, false otherwise
     * 
     * Side effects: None
     */
    private boolean isConjunction(String definition) {
        ArrayList<String> conjunction = m.getAllDefinitions().get("conjunction");
        return conjunction != null && conjunction.contains(definition);
    }

    /**
     * Validates if a definition belongs to other categories.
     * 
     * What it does: Currently returns false as a placeholder for future implementation.
     * 
     * Inputs:
     *   - definition: String - The definition text to validate
     * 
     * Return value: boolean - Always returns false
     * 
     * Side effects: None
     */
    private boolean isOther(String definition) {
        return false;
    }

    //endregion
}
