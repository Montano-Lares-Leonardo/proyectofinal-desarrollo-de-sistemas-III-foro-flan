package com.example.proyectoflan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class MainPageApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageApplication.class.getResource("main-page-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 650);
        primaryStage.setTitle("Página Principal");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("flanIcon.PNG"))));
        primaryStage.show();

        MainPageController controller = fxmlLoader.getController();
        controller.setPrimaryStageAndController(primaryStage, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
