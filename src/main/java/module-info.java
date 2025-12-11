/**
 * File: module-info.java
 * Purpose: Module descriptor for the Word Definition Game application
 * 
 * This module defines the dependencies and exports for the JavaFX-based Word Definition Game.
 * It requires JavaFX components for UI rendering, Gson for JSON parsing, and Java compiler/desktop APIs.
 * The module opens its package to both JavaFX FXML (for UI binding) and Gson (for JSON serialization).
 * 
 * Global Variables: None (module-level configuration only)
 * 
 * Key Module Declarations:
 * - requires: Declares dependencies on external modules (JavaFX, Gson, Java APIs)
 * - opens: Allows reflective access to the package by JavaFX and Gson
 * - exports: Makes the package publicly accessible to other modules
 */
module ca.additional.practice.javafx.worddefinitionsgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    requires com.google.gson;
    requires java.compiler;
    requires java.desktop;

    opens ca.additional.practice.javafx.worddefinitionsgame to javafx.fxml, com.google.gson;
    exports ca.additional.practice.javafx.worddefinitionsgame;
}