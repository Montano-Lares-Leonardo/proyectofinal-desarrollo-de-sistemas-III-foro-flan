package com.example.proyectoflan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ProfileController {
    @FXML private Label userText;
    @FXML private Label bioTextField;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button searchButton;
    @FXML private TextField searchBar;
    @FXML private TableView<Post> postTable;
    @FXML private TableColumn<Post, String> titleColumn;
    @FXML private TableColumn<Post, String> authorColumn;
    @FXML private TableColumn<Post, String> contentColumn;
    @FXML private ImageView pfpImage;
    private Stage thisStage;
    private Stage primaryStage;
    private MainPageController controller;
    private ObservableList<Post> listaObservable;
    User user;

    public void setStagesAndAlsoController(Stage primaryStage, Stage thisStage, MainPageController controller) {
        this.primaryStage = primaryStage;
        this.thisStage = thisStage;
        this.controller =controller;
    }

    public void setUser(User user) {
        this.user = user;
        load();
        if (LoggedIn.getYou() != user){
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    @FXML private void initialize(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("Text"));

        postTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                openPost(newSel);
            }
        });
    }

    @FXML private void reload(){
        user = ManejadorDB.getUsers(" WHERE u.user_ID = " + user.getID()).getFirst();
        load();
    }

    private void load(){
        userText.setText(user.getUsername());
        bioTextField.setText(user.getDesc());
        ArrayList<Post> posts = ManejadorDB.getPosts(" AND (p.user_ID = " + user.getID() + ")");
        if (posts != null){
            listaObservable = FXCollections.observableArrayList(posts);
            postTable.setItems(listaObservable);
        }
        try{
            pfpImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getPfp() + ".jpg"))));
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    private void openPost(Post post) {
        try {
            thisStage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("post-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 830, 600);

            PostController controller = fxmlLoader.getController();
            controller.setPost(post);

            //crear la subventana
            Stage newStage = new Stage();
            newStage.setTitle(post.getTitle());
            controller.setStagesAndController(primaryStage, newStage, this.controller);

            //mainStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icono.PNG"))));
            newStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("flanIcon.PNG"))));
            newStage.setScene(newScene);
            newStage.setX(primaryStage.getX() + (primaryStage.getWidth() - newScene.getWidth()) / 2);
            newStage.setY(primaryStage.getY() + (primaryStage.getHeight() - newScene.getHeight()) / 2);
            newStage.initOwner(primaryStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML private void deleteMyProfile(){
        if (ManejadorDB.deleteMyUser(user.getID())){
            JOptionPane.showMessageDialog(null, "Hasta luego " + user.getUsername() + "...");
            LoggedIn.logOut();
            controller.checkLogIn();
            thisStage.close();
        }
    }

    @FXML private void edit() throws IOException {
        thisStage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(LogInController.class.getResource("sign-up-screen-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 300, 400);

        //crear la subventana
        Stage newStage = new Stage();
        newStage.setTitle("Sign Up");
        SignUpController controller = fxmlLoader.getController();
        controller.setUser(LoggedIn.getYou());
        controller.setSelfAndController(newStage, this.controller);
        this.controller.openWindow(newStage, newScene);
        this.controller.checkLogIn();
    }
}
