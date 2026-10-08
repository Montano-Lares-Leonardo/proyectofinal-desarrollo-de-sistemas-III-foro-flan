package com.example.proyectoflan;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;

public class SignUpController {

    private Stage self;
    private MainPageController controller;
    @FXML
    private Label titleLbl;
    @FXML
    private TextField userBar;
    @FXML
    private TextField passwordBar;
    @FXML
    private TextField bioBar;
    @FXML
    private ComboBox<String> pfpComboBox;
    private User user;
    private String pfp = "'pfp/bepis'";

    public void setSelfAndController(Stage self, MainPageController controller) {
        this.self = self;
        this.controller = controller;
    }

    public void setUser(User user){
        this.user = user;
        userBar.setText(user.getUsername());
        passwordBar.setText(user.getPassword());
        bioBar.setText(user.getDesc());
    }

    @FXML private void initialize(){
        if (user != null) titleLbl.setText("EDITAR " + user.getUsername().toUpperCase());
        else titleLbl.setText("CREAR NUEVA CUENTA");
        pfpComboBox.getItems().addAll("albanian bob", "bepis", "dawgga", "marro", "redoggo", "serbian bob");
        pfpComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty())  pfp = "'pfp/bepis'";
            else pfp = "'pfp/" + newVal + "'";
        });
        if (user != null){
            titleLbl.setText("EDITAR USUARIO");
            userBar.setText(user.getUsername());
            passwordBar.setText(user.getPassword());
            bioBar.setText(user.getDesc());
        }
    }

    @FXML
    private void signUp() {
        if (userBar.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar un nombre.", "Error", Alert.AlertType.ERROR);
            userBar.requestFocus();
        } else if (passwordBar.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar una contraseña.", " Error", Alert.AlertType.ERROR);
            passwordBar.requestFocus();
        } else {
            String name = "'" + userBar.getText() + "'";
            String pass = "'" + passwordBar.getText() + "'";
            String bio;
            if (bioBar.getText().isEmpty()){
                bio = "NULL";
            } else {
                bio = "'" + bioBar.getText() + "'";
            }
            boolean exito = false;
            if (user == null){
                exito = ManejadorDB.insertarUsuario(name, pass, bio, pfp);
            } else {
                exito = ManejadorDB.editarUsuario(user.getID(), name, pass, bio, pfp);
            }
            if (exito) {
                mostrarAlerta("Usuario creado con éxito.", "Acción realizada con éxito", Alert.AlertType.INFORMATION);
                controller.checkLogIn();
                self.close();
            }
        }
    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.initOwner(self);

        alerta.showAndWait();
    }
}






