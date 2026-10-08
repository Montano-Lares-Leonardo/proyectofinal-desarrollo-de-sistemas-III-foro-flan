package com.example.proyectoflan;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class PostController {
    private Stage primaryStage;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button replyButton;
    @FXML
    private Label dateTxt;
    @FXML
    private Label titleTxt;
    @FXML
    private Label bodyTxt;
    @FXML
    private Label authorTxt;
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Post> replyTable;
    @FXML
    private TableColumn<Post, String> titleColumn;
    @FXML
    private TableColumn<Post, String> authorColumn;
    @FXML
    private TableColumn<Post, String> contentColumn;
    @FXML
    private Label responseTxt;
    private MainPageController controller;
    private Post publicacion;
    private User autor;
    private ObservableList<Post> listaObservable;
    private Stage thisStage;
    private Post parentPost;

    public void setPost(Post post) {
        publicacion = post;
        authorTxt.setText(post.getUsername());
        dateTxt.setText(post.getDate());
        titleTxt.setText(post.getTitle());
        bodyTxt.setText(post.getText());
        authorCheck();

        parentPost = ManejadorDB.getParentPost(post.getPostid());
        if (parentPost == null) {
            responseTxt.setVisible(false);
            responseTxt.setText("");
        } else {
            responseTxt.setText("← (Respondiendo a " + parentPost.getTitle() + ")");
        }

        reload();
        authorCheck();
    }

    private void authorCheck() {
        if (LoggedIn.loggedIn()){
            if (LoggedIn.getID() == publicacion.getUserID()) {
                editButton.setDisable(false);
                editButton.setDisable(false);
            } else{
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            }
            replyButton.setDisable(false);
        } else{
            replyButton.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    @FXML private void goToAuthor(){
        try {
            if (publicacion == null) {
                showAlert("Error", "No hay post seleccionado");
                return;
            }

            User author = ManejadorPost.getPostAutor(publicacion.getPostid());

            if (author == null) {
                showAlert("Error", "No se pudo encontrar el autor del post");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("profile-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 587, 600);

            ProfileController controller = fxmlLoader.getController();
            controller.setUser(author);

            //crear la subventana
            Stage newStage = new Stage();
            controller.setStagesAndAlsoController(primaryStage, newStage, this.controller);
            newStage.setTitle(author.getUsername());
            newStage.setScene(newScene);
            this.thisStage.close();
            this.controller.openWindow(newStage, newScene);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar el perfil del autor: " + e.getMessage());
        }
    }

    public void setStagesAndController(Stage primaryStage, Stage thisStage, MainPageController controller){
        this.primaryStage = primaryStage;
        this.thisStage = thisStage;
        this.controller = controller;
    }

    private void cargarSeleccionado(Post post) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("post-view.fxml"));
            Scene mainScene = new Scene(fxmlLoader.load(), 830, 600);

            PostController controller = fxmlLoader.getController();
            controller.setPost(post);
            this.thisStage.close();
            //crear la subventana
            Stage mainStage = new Stage();
            mainStage.setTitle(post.getTitle());
            controller.setStagesAndController(primaryStage, mainStage, this.controller);
            this.controller.openWindow(mainStage, mainScene);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("Text"));
    }

    @FXML private void reply() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("publish-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 500, 400);

        PublishController controller = fxmlLoader.getController();
        controller.setPrimaryStage(this.controller);
        controller.setParentPost(publicacion);

        //crear la subventana
        Stage newStage = new Stage();
        newStage.setTitle("Publicar");
        newStage.setScene(newScene);
        newStage.setX(primaryStage.getX() + 80);
        newStage.setY(primaryStage.getY() + 20);
        newStage.initOwner(primaryStage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
    }

    @FXML private void edit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("publish-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 500, 400);

        PublishController controller = fxmlLoader.getController();
        controller.setPrimaryStage(this.controller);
        controller.setPostParaEditar(publicacion);

        //crear la subventana
        Stage newStage = new Stage();
        newStage.setTitle(LoggedIn.getYou().getUsername());
        newStage.setScene(newScene);
        newStage.setX(primaryStage.getX() + 80);
        newStage.setY(primaryStage.getY() + 20);
        newStage.initOwner(primaryStage);
        newStage.initModality(Modality.WINDOW_MODAL);
        thisStage.close();
        newStage.show();
    }

    @FXML private void delete(){
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar eliminación");
            confirmAlert.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar el post
                boolean deletedPost = ManejadorPost.deletePost(publicacion.getPostid());

                if (deletedPost) {
                    showAlert("Éxito", "Post eliminado correctamente");
                    this.controller.reload();
                    thisStage.close();
                } else {
                    showAlert("Error!!!", "No se pudo eliminar el post");
                }
            }

        } catch (Exception ioException){
            ioException.printStackTrace();
            showAlert("Error!!", "No se pudo eliminar el post");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void buscar(){
        if (!searchBar.getText().isEmpty()){
            String search = searchBar.getText().toLowerCase();
            ArrayList<Post> posts = ManejadorDB.getPosts(" AND (p.post_parent_ID = " + publicacion.getPostid() + ") AND (p.title LIKE '%" + search + "%' OR p.body LIKE '%" + search + "%')");
            if (posts != null){
                listaObservable = FXCollections.observableArrayList(posts);
                replyTable.setItems(listaObservable);
            }
        } else {
            reload();
        }
    }

    @FXML private void reload(){
        listaObservable = FXCollections.observableArrayList(ManejadorDB.getPosts(" AND (p.post_parent_ID = " + publicacion.getPostid() + ")"));
        replyTable.setItems(listaObservable);
        replyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarSeleccionado(newSel);
            }
        });
    }

    @FXML private void goToParent(){
        if (parentPost != null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("post-view.fxml"));
                Scene mainScene = new Scene(fxmlLoader.load(), 830, 600);

                PostController controller = fxmlLoader.getController();
                controller.setPost(parentPost);

                this.thisStage.close();

                //crear la subventana
                Stage mainStage = new Stage();
                mainStage.setTitle(parentPost.getTitle());
                controller.setStagesAndController(primaryStage, mainStage, this.controller);
                this.controller.openWindow(mainStage, mainScene);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
