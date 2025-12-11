/*
 * File: module-info.java
 * Purpose: Java module descriptor that defines the module structure, dependencies,
 *          and exports for the Word Definitions Game application.
 * 
 * Global Variables: None
 * 
 * Major Components:
 *   - Module declaration: Defines the ca.additional.practice.javafx.worddefinitionsgame module
 *   - Required modules: javafx.controls, javafx.fxml, bootstrapfx.core, gson, java.compiler, java.desktop
 *   - Opens: Makes the package accessible to javafx.fxml and gson for reflection
 *   - Exports: Makes the package available to other modules
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