/**
 * File: Launcher.java
 * Purpose: Launcher class that provides the main entry point for the Word Definition Game
 * 
 * This class contains the main method which launches the JavaFX application. It's separated from
 * HelloApplication to avoid issues with JavaFX module system and provides a clean entry point
 * for running the application.
 * 
 * Global Variables: None
 * 
 * Key Classes/Methods:
 * - Launcher: Wrapper class containing the main method
 * - main(): Entry point that launches the HelloApplication
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.application.Application;

public class Launcher {
    /**
     * Main entry point for the Word Definition Game application.
     * 
     * This method launches the JavaFX application by calling Application.launch() with
     * HelloApplication class and command-line arguments. Using a separate launcher class
     * helps avoid module-related issues in JavaFX applications.
     * 
     * Inputs:
     * - args: Command-line arguments passed to the application (typically unused)
     * 
     * Return value: void
     * 
     * Side effects:
     * - Initializes the JavaFX runtime
     * - Creates and starts the HelloApplication instance
     * - Blocks until the application window is closed
     */
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }
}
