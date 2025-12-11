/**
 * File: HelloApplication.java
 * Purpose: Main JavaFX Application class that serves as the entry point for the Word Definition Game
 * 
 * This class extends the JavaFX Application class and is responsible for initializing the primary
 * stage (window) of the application. It loads the FXML layout file and sets up the initial scene
 * with predefined dimensions.
 * 
 * Global Variables: None
 * 
 * Key Classes/Methods:
 * - HelloApplication: JavaFX Application subclass that initializes the game window
 * - start(): Configures and displays the primary stage with the game UI
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    /**
     * Initializes and displays the primary stage (main window) of the Word Definition Game.
     * 
     * This method is called by the JavaFX framework when the application starts. It loads the
     * game.fxml file to construct the UI, creates a scene with fixed dimensions, sets the window
     * title, and displays the stage.
     * 
     * Inputs:
     * - stage: The primary Stage object provided by JavaFX framework
     * 
     * Return value: void
     * 
     * Side effects:
     * - Loads the game.fxml resource file
     * - Creates a Scene with dimensions 600x565 pixels
     * - Sets the stage title to "Hello!"
     * - Makes the stage visible to the user
     * 
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 565);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
