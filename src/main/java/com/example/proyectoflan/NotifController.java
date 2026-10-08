package com.example.proyectoflan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

public class NotifController {
    @FXML private TextField searchBar;
    @FXML private TableView<Notif> postTable;
    @FXML private TableColumn<Notif, String> titleColumn;
    @FXML private TableColumn<Notif, String> authorColumn;
    @FXML private TableColumn<Notif, String> contentColumn;
    private ObservableList<Notif> listaObservable;
    MainPageController controller;
    private Stage primaryStage;
    private Stage thisStage;
    public void setStages(Stage primaryStage, Stage thisStage, MainPageController controller) {
        this.primaryStage = primaryStage;
        this.thisStage = thisStage;
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        titleColumn.setText("Causa");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Read"));
        authorColumn.setText("Abierto");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("Cause"));
        contentColumn.setText("Publicacion");

        reload();
        postTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                openPost(newSel.getPost(), newSel.getId());
            }
        });
    }

    private void openPost(Post post, int notif) {
        try {
            ManejadorDB.setNotifRead(notif);
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
            thisStage.close();
            newStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML public void reload(){
        ArrayList<Notif> notifs = ManejadorDB.getNotifs("");
        if (notifs != null){
            listaObservable = FXCollections.observableArrayList(notifs);
            postTable.setItems(listaObservable);
        }
    }

    @FXML public void search(){
        if (!searchBar.getText().isEmpty()){
            String search = searchBar.getText().toLowerCase();
            ArrayList<Notif> notifs = ManejadorDB.getNotifs(" AND (n.title LIKE '%" + search + "%' OR p.title LIKE '%" + search + "%')");
            if (notifs != null){
                listaObservable = FXCollections.observableArrayList(notifs);
                postTable.setItems(listaObservable);
            }
        } else {
            reload();
        }
    }
}