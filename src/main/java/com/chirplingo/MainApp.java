package com.chirplingo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.chirplingo.utils.Config;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label helloLabel = new Label("Hello World");

        StackPane root = new StackPane();
        root.getChildren().add(helloLabel);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Chirplingo App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Config.load();

        launch(args);
    }
}