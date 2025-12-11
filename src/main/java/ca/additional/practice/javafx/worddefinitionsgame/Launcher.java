/*
 * File: Launcher.java
 * Purpose: Entry point launcher class that starts the JavaFX application.
 *          Separates the main method from the Application class to avoid module-related issues.
 * 
 * Global Variables: None
 * 
 * Major Classes/Functions:
 *   - Launcher: Launcher class with main method
 *   - main(String[]): Entry point that launches the HelloApplication
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import javafx.application.Application;

public class Launcher {
    /**
     * Main entry point for the application.
     * 
     * What it does: Launches the HelloApplication JavaFX application with provided arguments.
     * 
     * Inputs:
     *   - args: Command line arguments passed to the application
     * 
     * Return value: void
     * 
     * Side effects: Starts the JavaFX application lifecycle
     */
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }
}
