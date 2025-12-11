/*
 * File: HelloApplication.java
 * Purpose: Main JavaFX application class that serves as the entry point for the Word Definition Game.
 *          Initializes and displays the primary game window with the FXML-defined UI.
 * 
 * Global Variables: None
 * 
 * Major Classes/Functions:
 *   - HelloApplication: Extends JavaFX Application class to launch the game
 *   - start(Stage): Initializes and displays the game window
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    /**
     * Starts the JavaFX application and initializes the game window.
     * 
     * What it does: Loads the game.fxml file, creates a Scene with specified dimensions,
     *               sets the window title, and displays the stage.
     * 
     * Inputs:
     *   - stage: The primary Stage object provided by JavaFX framework
     * 
     * Return value: void
     * 
     * Side effects: Creates and displays the game window, loads FXML resources
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
