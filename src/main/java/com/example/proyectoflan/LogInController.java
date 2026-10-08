package com.example.proyectoflan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;

public class LogInController {
    private Stage self;
    private MainPageController controller;
    @FXML private TextField userBar;
    @FXML private PasswordField passwordBar;
    @FXML private CheckBox rememberCheck;
    public void setSelfAndController(Stage self, MainPageController controller) {
        this.self = self;
        this.controller = controller;
    }

    @FXML private void login(){
        String name = userBar.getText();
        String pass = passwordBar.getText();
        if (ManejadorDB.checkUser(name, pass)){
            User user = ManejadorDB.getUsers(" WHERE u.username = '" + name + "' AND u.password = '" + pass + "'").getFirst();
            LoggedIn.logIn(user);
            controller.reload();
            controller.checkLogIn();
        }else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña no son correctos.");
        }
        self.close();
    }

    @FXML private void reload(){
        userBar.setText("");
        passwordBar.setText("");
    }

    @FXML private void signup() throws IOException {
        self.close();
        FXMLLoader fxmlLoader = new FXMLLoader(LogInController.class.getResource("sign-up-screen-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 300, 400);

        //crear la subventana
        Stage newStage = new Stage();
        newStage.setTitle("Sign Up");
        SignUpController controller = fxmlLoader.getController();
        controller.setSelfAndController(newStage, this.controller);
        this.controller.openWindow(newStage, newScene);
    }
}
