package com.example.proyectoflan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainPageController {
    private Stage primaryStage;
    private MainPageController controller;
    @FXML private TextField searchBar;
    @FXML private Button loginButton;
    @FXML private Button postButton;
    @FXML private Button notifButton;
    @FXML private TableView<Post> postTable;
    @FXML private TableColumn<Post, String> titleColumn;
    @FXML private TableColumn<Post, String> authorColumn;
    @FXML private TableColumn<Post, String> contentColumn;

    private ObservableList<Post> listaObservable;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("Text"));

        reload();
        postTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                openPost(newSel);
            }
        });

        checkLogIn();
    }

    @FXML
    private void openLogInOrUser() throws IOException {
        if (LoggedIn.loggedIn()){
            FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("profile-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 587, 600);

            ProfileController controller = fxmlLoader.getController();
            controller.setUser(LoggedIn.getYou());

            //crear la subventana
            Stage newStage = new Stage();
            controller.setStagesAndAlsoController(primaryStage, newStage, this.controller);
            newStage.setTitle(LoggedIn.getYou().getUsername());
            newStage.setScene(newScene);
            openWindow(newStage, newScene);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("login-screen-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 300, 227);

            LogInController controller = fxmlLoader.getController();

            //crear la subventana
            Stage newStage = new Stage();
            controller.setSelfAndController(newStage, this.controller);
            newStage.setTitle("Login");
            openWindow(newStage, newScene);
        }
    }

    public void openWindow(Stage newStage, Scene newScene) {
        newStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("flanIcon.PNG"))));
        newStage.setScene(newScene);
        newStage.setX(primaryStage.getX() + (primaryStage.getWidth() - newScene.getWidth()) / 2);
        newStage.setY(primaryStage.getY() + (primaryStage.getHeight() - newScene.getHeight()) / 2);
        newStage.initOwner(primaryStage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
    }

    @FXML public void reload(){
        ArrayList<Post> posts = ManejadorDB.getPosts(" AND (p.post_parent_ID IS NULL)");
        if (posts != null){
            listaObservable = FXCollections.observableArrayList(posts);
            postTable.setItems(listaObservable);
        }
    }

    @FXML private void buscar(){
        if (!searchBar.getText().isEmpty()){
            String search = searchBar.getText().toLowerCase();
            ArrayList<Post> posts = ManejadorDB.getPosts(" AND (p.post_parent_ID IS NULL) AND (p.title LIKE '%" + search + "%' OR p.body LIKE '%" + search + "%')");
            if (posts != null){
                listaObservable = FXCollections.observableArrayList(posts);
                postTable.setItems(listaObservable);
            }
        } else {
            reload();
        }
    }

    public void checkLogIn() {
        if (LoggedIn.loggedIn()){
            loginButton.setText(LoggedIn.getYou().getUsername());
            notifButton.setDisable(false);
            postButton.setDisable(false);
        } else {
            loginButton.setText("Login");
            notifButton.setDisable(true);
            postButton.setDisable(true);
        }
    }

    @FXML private void createPost() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("publish-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 500, 400);

        PublishController controller = fxmlLoader.getController();
        controller.setPrimaryStage(this.controller);

        //crear la subventana
        Stage newStage = new Stage();
        newStage.setTitle(LoggedIn.getYou().getUsername());
        openWindow(newStage, newScene);
    }

    @FXML private void openNotif() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("notif-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 625, 500);

        NotifController controller = fxmlLoader.getController();

        //crear la subventana
        Stage newStage = new Stage();
        controller.setStages(primaryStage, newStage, this.controller);
        newStage.setTitle(LoggedIn.getYou().getUsername());
        openWindow(newStage, newScene);
    }

    private void openPost(Post post) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("post-view.fxml"));
            Scene mainScene = new Scene(fxmlLoader.load(), 830, 600);

            PostController controller = fxmlLoader.getController();
            controller.setPost(post);

            //crear la subventana
            Stage mainStage = new Stage();
            mainStage.setTitle(post.getTitle());
            controller.setStagesAndController(primaryStage, mainStage, this.controller);
            openWindow(mainStage, mainScene);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setPrimaryStageAndController(Stage primaryStage, MainPageController controller){
        this.controller = controller;
        this.primaryStage = primaryStage;
    }
}
