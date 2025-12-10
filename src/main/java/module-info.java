module ca.additional.practice.javafx.worddefinitionsgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    requires com.google.gson;

    opens ca.additional.practice.javafx.worddefinitionsgame to javafx.fxml, com.google.gson;
    exports ca.additional.practice.javafx.worddefinitionsgame;
}