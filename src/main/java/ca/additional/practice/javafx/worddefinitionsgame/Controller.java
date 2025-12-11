/**
 * File: Controller.java
 * Purpose: MVC Controller class that handles UI events and game logic for the Word Definition Game
 * 
 * This controller manages the interactive drag-and-drop gameplay where users match word definitions
 * to their corresponding parts of speech. It handles mouse events for dragging labels, validates
 * answers against the model data, updates the score, and manages the game flow.
 * 
 * Global Variables:
 * - root: AnchorPane - The root container for all UI elements
 * - d1, d2, d3: Label - Three labels displaying word definitions
 * - wordLabel: Label - Displays the current word being defined
 * - scoreLabel: Label - Displays the current player score
 * - labelOriginalPostion: Map<Label, double[]> - Stores original positions of definition labels
 * - nounPane, verbPane, adjectivePane, adverbPane, prepositionPane, conjunctionPane, otherPane: Pane - Drop zones for different parts of speech
 * - panes: ArrayList<Pane> - Collection of all panes in the root container
 * - definitionsLabels: ArrayList<Label> - Collection of all definition labels
 * - currentPosition: double[] - Tracks current mouse position during drag
 * - orginalPosition: double[] - Stores original position before drag
 * - score: int - Current player score
 * - points: Map<String, Integer> - Point values for different game outcomes
 * - log: Map<Integer, List<Integer>> - Game activity log
 * - AnswerLog: int[] - Tracks answer history
 * - m: Model - Instance of the game model containing word and definitions
 * - wordAPI: WordAPI - Service for fetching random words
 * - dictionairyAPI: DictionairyAPI - Service for fetching word definitions
 * - wordsFromFile: WordsFromFile - Fallback word source from local file
 * 
 * Key Classes/Methods:
 * - Controller: Main controller class implementing game logic and UI interaction
 * - initialize(): Sets up initial game state and UI bindings
 * - game(): Loads new word and definitions into the UI
 * - handleNewWord(): Resets game state and loads next word
 * - Mouse event handlers: setMousePressed(), setMouseDragged(), setMouseReleased()
 * - Validation methods: isNoun(), isVerb(), isAdjective(), isAdverb(), isPreposition(), isConjunction()
 * - Feedback methods: isCorrect(), isIncorrect(), isSkipped()
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
     * Initializes the controller and sets up the game environment.
     * 
     * This method is automatically called by JavaFX after the FXML file is loaded. It initializes
     * all class instances (APIs and model), populates the definitions labels array, stores original
     * label positions, sets up the scoring system, collects all panes from the root container,
     * and starts the initial game setup.
     * 
     * Inputs: None (called automatically by JavaFX framework)
     * 
     * Return value: void
     * 
     * Side effects:
     * - Creates instances of WordAPI, DictionairyAPI, Model, and WordsFromFile
     * - Populates definitionsLabels ArrayList with d1, d2, d3
     * - Stores original positions of labels in labelOriginalPostion map
     * - Initializes points map with scoring values (correct: +100, wrong: -50, etc.)
     * - Collects all Pane nodes from root into panes ArrayList
     * - Calls setup() and game() to begin gameplay
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
     * Sets up the initial display by showing the current word.
     * 
     * This method retrieves the word from the model and displays it in the wordLabel.
     * It's called once during initialization to show the first word.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects:
     * - Updates wordLabel text with the current word from model
     */
    private void setup() {
        wordLabel.setText(m.getWord());
    }

    /**
     * Handles the "Next Word" button click event to load a new word.
     * 
     * This method resets the game state by creating a new Model instance (which fetches a new
     * word and definitions), then calls game() to display them. It's triggered when the user
     * clicks the "Next" button in the UI.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects:
     * - Creates a new Model instance, discarding previous word data
     * - Calls game() to update UI with new word and definitions
     * - Prints debug information to console
     */
    public void handleNewWord(){
        m = new Model(); //Reset the data
        game(); //Run game

        System.out.println("Next word");
        System.out.println(definitionsLabels.size());
    }


    // Game functions
   /**
    * Main game loop that updates the UI with current word and definitions.
    * 
    * This method displays the current word in the wordLabel and populates the definition labels
    * with definitions from the model. It ensures there are exactly 3 definition labels by creating
    * new ones if needed, then sets the text and mouse event handlers for each label to enable
    * drag-and-drop functionality.
    * 
    * Inputs: None
    * 
    * Return value: void
    * 
    * Side effects:
    * - Updates wordLabel with current word from model
    * - Creates missing definition labels if fewer than 3 exist
    * - Sets text of definition labels from model's definition list
    * - Attaches mouse event handlers (pressed, dragged, released) to each label
    * - Prints debug messages to console
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
    * This method is called when the user clicks on a definition label to begin dragging.
    * Currently prints a debug message and stores reference to the label being dragged.
    * 
    * Inputs:
    * - event: MouseEvent containing information about the mouse press action
    * 
    * Return value: void
    * 
    * Side effects:
    * - Prints "Pressed" to console
    * - Retrieves the Label that was clicked
    */
   private void setMousePressed(MouseEvent event){
       System.out.println("Pressed");
        Label l = (Label) event.getSource();
   }

   /**
    * Handles mouse drag events to move definition labels around the screen.
    * 
    * This method is continuously called while the user drags a definition label. It updates
    * the label's position to follow the mouse cursor by setting its layout coordinates to
    * the current mouse scene coordinates.
    * 
    * Inputs:
    * - event: MouseEvent containing current mouse position during drag
    * 
    * Return value: void
    * 
    * Side effects:
    * - Updates currentPosition array with current mouse X and Y coordinates
    * - Changes the label's layoutX and layoutY to move it on screen
    */
   private void setMouseDragged(MouseEvent event){
       Label l = (Label) event.getSource();

       currentPosition[0] = event.getSceneX();
       currentPosition[1] = event.getSceneY();

       l.setLayoutX(currentPosition[0]);
       l.setLayoutY(currentPosition[1]);
   }

   /**
    * Handles mouse release events when user drops a definition label.
    * 
    * This method is called when the user releases a definition label after dragging. It determines
    * which pane (if any) the label was dropped onto and validates whether the definition matches
    * that part of speech. Correct matches remove the label and add points, incorrect matches
    * deduct points and return the label to its original position, and dropping in the "Other"
    * pane skips the definition. If dropped outside all panes, the label returns to original position.
    * 
    * Inputs:
    * - event: MouseEvent containing mouse position when released
    * 
    * Return value: void
    * 
    * Side effects:
    * - Checks if label is within any part-of-speech pane
    * - Calls isCorrect() if match is valid (removes label, adds 100 points)
    * - Calls isIncorrect() if match is invalid (deducts 50 points, returns label)
    * - Calls isSkipped() if dropped in otherPane (removes label, no points)
    * - Returns label to original position if dropped outside all panes
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
    * Handles a correct answer by removing the label and awarding points.
    * 
    * This method is called when a definition is correctly matched to its part of speech.
    * It removes the label from the UI and adds the "correct" point value (100 points) to
    * the score, then updates the score display.
    * 
    * Inputs:
    * - l: Label - The definition label that was correctly matched
    * 
    * Return value: void
    * 
    * Side effects:
    * - Removes the label from the root container (making it disappear)
    * - Adds 100 points to the score
    * - Updates scoreLabel text to show new score
    */
   private void isCorrect(Label l){
       root.getChildren().remove(l);
       score += points.get("correct");
       scoreLabel.setText("Score: " + score);
   }

   /**
    * Handles an incorrect answer by deducting points and returning the label.
    * 
    * This method is called when a definition is incorrectly matched to a part of speech.
    * It deducts the "wrong" point value (-50 points) from the score, updates the score display,
    * and moves the label back to its original position so the user can try again.
    * 
    * Inputs:
    * - l: Label - The definition label that was incorrectly matched
    * 
    * Return value: void
    * 
    * Side effects:
    * - Deducts 50 points from the score
    * - Updates scoreLabel text to show new score
    * - Returns the label to its original layout position
    */
   private void isIncorrect(Label l){
       score += points.get("wrong"); //Value -50
       scoreLabel.setText("Score: " + score);

       //relocate the definitions to their original location
       l.setLayoutX(labelOriginalPostion.get(l)[0]);
       l.setLayoutY(labelOriginalPostion.get(l)[1]);
   }

   /**
    * Handles a skipped definition by removing the label without affecting score.
    * 
    * This method is called when a user drops a definition in the "Other" pane, indicating
    * they want to skip it without guessing. The label is removed from the UI but no points
    * are added or deducted.
    * 
    * Inputs:
    * - l: Label - The definition label to skip
    * 
    * Return value: void
    * 
    * Side effects:
    * - Removes the label from the root container
    * - Score remains unchanged
    */
   private void isSkipped(Label l){
        root.getChildren().remove(l);
   }

   /**
    * Checks if the mouse position is within the bounds of a specific pane.
    * 
    * This helper method determines whether the mouse cursor is positioned over a given pane
    * by comparing the mouse coordinates to the pane's position and dimensions. Used to detect
    * which part-of-speech category a label is dropped onto.
    * 
    * Inputs:
    * - event: MouseEvent containing current mouse scene coordinates
    * - pane: Pane to check if mouse is within its bounds
    * 
    * Return value: boolean - true if mouse is within pane bounds, false otherwise
    * 
    * Side effects: None (pure calculation)
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
    /**
     * Checks if a given definition belongs to the "noun" part of speech.
     * 
     * This method queries the model's definitions map to see if the provided definition
     * text exists in the list of noun definitions for the current word.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a noun, false otherwise
     * 
     * Side effects: None (read-only check)
     */
    private boolean isNoun(String definition) {
        ArrayList<String> nouns = m.getAllDefinitions().get("noun");
        return nouns != null && nouns.contains(definition);
    }

    /**
     * Checks if a given definition belongs to the "verb" part of speech.
     * 
     * This method queries the model's definitions map to see if the provided definition
     * text exists in the list of verb definitions for the current word.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a verb, false otherwise
     * 
     * Side effects: None (read-only check)
     */
    private boolean isVerb(String definition) {
        ArrayList<String> verb = m.getAllDefinitions().get("verb");
        return verb != null && verb.contains(definition);
    }

    /**
     * Checks if a given definition belongs to the "adjective" part of speech.
     * 
     * This method queries the model's definitions map to see if the provided definition
     * text exists in the list of adjective definitions for the current word.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is an adjective, false otherwise
     * 
     * Side effects: None (read-only check)
     */
    private boolean isAdjective(String definition) {
        ArrayList<String> adjective = m.getAllDefinitions().get("adjective");
        return adjective != null && adjective.contains(definition);
    }

    /**
     * Checks if a given definition belongs to the "adverb" part of speech.
     * 
     * This method queries the model's definitions map to see if the provided definition
     * text exists in the list of adverb definitions for the current word.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is an adverb, false otherwise
     * 
     * Side effects: None (read-only check)
     */
    private boolean isAdverb(String definition) {
        ArrayList<String> adverb = m.getAllDefinitions().get("adverb");
        return adverb != null && adverb.contains(definition);
    }

    /**
     * Checks if a given definition belongs to the "preposition" part of speech.
     * 
     * This method queries the model's definitions map to see if the provided definition
     * text exists in the list of preposition definitions for the current word.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a preposition, false otherwise
     * 
     * Side effects: None (read-only check)
     */
    private boolean isPreposition(String definition) {
        ArrayList<String> preposition = m.getAllDefinitions().get("preposition");
        return preposition != null && preposition.contains(definition);
    }

    /**
     * Checks if a given definition belongs to the "conjunction" part of speech.
     * 
     * This method queries the model's definitions map to see if the provided definition
     * text exists in the list of conjunction definitions for the current word.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - true if definition is a conjunction, false otherwise
     * 
     * Side effects: None (read-only check)
     */
    private boolean isConjunction(String definition) {
        ArrayList<String> conjunction = m.getAllDefinitions().get("conjunction");
        return conjunction != null && conjunction.contains(definition);
    }

    /**
     * Placeholder method for checking if a definition belongs to "other" parts of speech.
     * 
     * This method is currently not implemented and always returns false. It could be extended
     * to handle parts of speech not covered by the main categories.
     * 
     * Inputs:
     * - definition: String - The definition text to validate
     * 
     * Return value: boolean - always false in current implementation
     * 
     * Side effects: None
     */
    private boolean isOther(String definition) {
        return false;
    }

    //endregion
}
