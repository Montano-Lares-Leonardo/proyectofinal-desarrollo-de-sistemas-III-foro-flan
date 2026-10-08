package com.example.proyectoflan;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PublishController implements Initializable{
    /*MainPageController controller;
    public void setPrimaryStage(MainPageController controller) {
        this.controller = controller;
    }
    //clase vacia

    @FXML
    private TextField titleBar;

    @FXML
    private TextField contentBar;

    @FXML
    private CheckBox privateCheckBox;

    @FXML
    private Button visibilityButton;

    private ListView<String> selectedUsersList;

    @FXML
    private Button postButton;

    private Post parentPost = null;

    private ObservableList<String> usersSeleccionados = FXCollections.observableArrayList();
    private ArrayList<Integer> idUserSeleccionado = new ArrayList<>();

    private User user;

    private Post postParaEditar;

    private boolean esEditable = false;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        setupEventHandlers();
        privateCheckBox.setVisible(false);
    }

    public void setParentPost(Post post){
        parentPost = post;
    }

    private void setupUI() {
        titleBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 200) {
                titleBar.setText(oldValue);
            }
            validateTitle();
        });

        contentBar.textProperty().addListener((observable, oldValue, newValue) -> {
            validateContent();
        });

        if (selectedUsersList != null){
            selectedUsersList.setItems(usersSeleccionados);
            selectedUsersList.setVisible(false);
        }

        privateCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            visibilityButton.setVisible(newValue);
            if (selectedUsersList != null) selectedUsersList.setVisible(newValue);
        });

        visibilityButton.setVisible(false);
    }

    private void setupEventHandlers() {

        postButton.setOnAction(event -> {
            if (validateForm()) {
                if (esEditable) {
                    updatePost();
                } else {
                    createPost();
                }
            }
        });
    }


    private void handleSelectedUsers(List<User> users) {
        usersSeleccionados.clear();
        idUserSeleccionado.clear();

        for (User user : users) {
            usersSeleccionados.add(user.getUsername());
            idUserSeleccionado.add(user.getUserId());
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validar título
        if (titleBar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,"Pon un título!!!!");
            isValid = false;
        } else if (titleBar.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(null,"El título debe tener al menos 3 caracteres!!!");
            isValid = false;
        }
        // Validar contenido
        if (contentBar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,"Pon contenido!!!");
            isValid = false;
        } else if (contentBar.getText().trim().length() < 10) {
            JOptionPane.showMessageDialog(null,"El contenido debe tener al menos 10 caracteres!!!");
            isValid = false;
        }
        return isValid;
    }

    private void validateTitle() {
        if (titleBar.getText().trim().length() > 200) {
            JOptionPane.showMessageDialog(null,"Máximo 200 caracteres!!!!");
        }
    }

    private void validateContent() {
        if (contentBar.getText().trim().length() > 10000) {
            JOptionPane.showMessageDialog(null,"Mucho contenidooo!!!");
        }
    }

    private void createPost() {
        try {
            Post post = new Post();
            post.setTitle(titleBar.getText().trim());
            post.setText(contentBar.getText().trim());
            post.setAuthor(LoggedIn.getID());
            post.setPriv(privateCheckBox.isSelected());
            if (privateCheckBox.isSelected() && !idUserSeleccionado.isEmpty()) {
                post.setAllowedUsers(idUserSeleccionado);
            }

            boolean yay = ManejadorPublish.createPost(post, parentPost);

            if (yay) {
                showAlert("Éxito¡¡¡", "Post publicado correctamente");
                if (controller != null) controller.reload();
                closeWindow();
            } else {
                showAlert("Error!!!", "No se pudo publicar el post");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error!!!", "Error al publicar: " + e.getMessage());
        }
    }

    private void updatePost() {
        try {
            if (postParaEditar == null) return;

            postParaEditar.setTitle(titleBar.getText().trim());
            postParaEditar.setText(contentBar.getText().trim());
            postParaEditar.setPriv(privateCheckBox.isSelected());

            // Si es privado y hay usuarios seleccionados, agregarlos
            if (privateCheckBox.isSelected() && !idUserSeleccionado.isEmpty()) {
                postParaEditar.setAllowedUsers(idUserSeleccionado);
            } else {
                postParaEditar.setAllowedUsers(new ArrayList<>());
            }

            boolean success = ManejadorPublish.updatePost(postParaEditar);

            if (success) {
                showAlert("Éxito", "Post actualizado correctamente");
                closeWindow();
            } else {
                showAlert("Error", "No se pudo actualizar el post");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setPostParaEditar(Post post) {
        this.postParaEditar = post;
        this.esEditable = true;

        if (post != null) {
            titleBar.setText(post.getTitle());
            contentBar.setText(post.getText());
            privateCheckBox.setSelected(post.isPriv());
            postButton.setText("Actualizar");

            if (post.isPriv()) {
                loadAllowedUsersForPost(post.getPostid());
                visibilityButton.setVisible(true);
                selectedUsersList.setVisible(true);
            }
        }
    }

    private void loadAllowedUsersForPost(int postId) {
        try {
            ArrayList<User> allowedUsers = ManejadorPublish.getAllowedUsersForPost(postId);
            handleSelectedUsers(allowedUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) postButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void openPostAccess() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageController.class.getResource("post-access-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 587, 600);

        PostAccessController controller = fxmlLoader.getController();

        //crear la subventana
        Stage newStage = new Stage();
        newStage.setTitle("Post access");
        newStage.setScene(newScene);
        newStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("flanIcon.PNG"))));
        newStage.setScene(newScene);
        newStage.initOwner(postButton.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
    }*/

    MainPageController controller;

    public void setPrimaryStage(MainPageController controller) {
        this.controller = controller;
    }

    @FXML
    private TextField titleBar;

    @FXML
    private TextField contentBar;

    @FXML
    private CheckBox privateCheckBox;

    @FXML
    private Button visibilityButton;

    private ListView<String> selectedUsersList;

    @FXML
    private Button postButton;

    private Post parentPost = null;

    private ObservableList<String> usersSeleccionados = FXCollections.observableArrayList();
    private ArrayList<Integer> idUserSeleccionado = new ArrayList<>();
    private ArrayList<User> allowedUsersForNewPost = new ArrayList<>();

    private User user;
    private Post postParaEditar;
    private boolean esEditable = false;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        setupEventHandlers();
        visibilityButton.setVisible(false);
    }

    public void setParentPost(Post post) {
        parentPost = post;
    }

    private void setupUI() {
        titleBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 200) {
                titleBar.setText(oldValue);
            }
            validateTitle();
        });

        contentBar.textProperty().addListener((observable, oldValue, newValue) -> {
            validateContent();
        });
    }

    private void setupEventHandlers() {
        visibilityButton.setOnAction(event -> {
            try {
                openPostAccess();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo abrir la ventana de configuración de acceso: " + e.getMessage());
            }
        });

        privateCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            visibilityButton.setVisible(newValue);

            if (!newValue) {
                allowedUsersForNewPost.clear();
                updateSelectedUsersList();
            }

        });

        postButton.setOnAction(event -> {
            if (validateForm()) {
                if (esEditable) {
                    updatePost();
                } else {
                    createPost();
                }
            }
        });
    }

    @FXML
    private void openPostAccess() throws IOException {
        if (esEditable && postParaEditar != null) {
            openPostAccessForExistingPost();
        } else {
            openPostAccessForNewPost();
        }
    }

    private void openPostAccessForNewPost() throws IOException {
        Post tempPost = new Post();
        tempPost.setTitle(titleBar.getText().trim());
        tempPost.setText(contentBar.getText().trim());
        tempPost.setAuthor(LoggedIn.getID());
        tempPost.setPriv(true);

        openPostAccessWindow(tempPost, "Configurar Acceso para Nuevo Post");
    }

    private void openPostAccessForExistingPost() throws IOException {
        openPostAccessWindow(postParaEditar, "Configurar Acceso para: " + postParaEditar.getTitle());
    }

    private void openPostAccessWindow(Post post, String title) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("post-access-view.fxml"));

        if (fxmlLoader.getLocation() == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("postaccess.fxml"));
        }

        if (fxmlLoader.getLocation() == null) {
            throw new IOException("No se pudo encontrar el archivo FXML de acceso al post");
        }

        Scene newScene = new Scene(fxmlLoader.load(), 500, 600);

        PostAccessController accessController = fxmlLoader.getController();
        accessController.setPostData(post);
        accessController.setCurrentUser(LoggedIn.getYou());

        accessController.setAllowedUsersCallback(allowedUsers -> {
            this.allowedUsersForNewPost = allowedUsers;
            updateSelectedUsersList();
        });

        Stage newStage = new Stage();
        newStage.setTitle(title);
        newStage.setScene(newScene);

        PostAccessController controller = fxmlLoader.getController();
        controller.getThisStage(newStage);

        try {
            Image icon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("flanIcon.PNG")));
            newStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        newStage.initOwner(postButton.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);

        newStage.showAndWait();
    }

    private void updateSelectedUsersList() {
        usersSeleccionados.clear();
        idUserSeleccionado.clear();

        for (User user : allowedUsersForNewPost) {
            usersSeleccionados.add(user.getUsername());
            idUserSeleccionado.add(user.getID());
        }

        if (allowedUsersForNewPost.size() > 0) {
            visibilityButton.setText("Configurar Acceso (" + allowedUsersForNewPost.size() + " usuarios)");
        } else {
            visibilityButton.setText("Configurar Acceso");
        }
    }

    private ArrayList<Integer> convertUsersToIds(ArrayList<User> users) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getID());
        }
        return ids;
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (titleBar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pon un título!!!!");
            isValid = false;
        } else if (titleBar.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(null, "El título debe tener al menos 3 caracteres!!!");
            isValid = false;
        }

        if (contentBar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pon contenido!!!");
            isValid = false;
        } else if (contentBar.getText().trim().length() < 10) {
            JOptionPane.showMessageDialog(null, "El contenido debe tener al menos 10 caracteres!!!");
            isValid = false;
        }

        if (privateCheckBox.isSelected() && allowedUsersForNewPost.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Un post privado debe tener al menos un usuario permitido!!!");
            isValid = false;
        }

        return isValid;
    }

    private void validateTitle() {
        if (titleBar.getText().trim().length() > 200) {
            JOptionPane.showMessageDialog(null, "Máximo 200 caracteres!!!!");
        }
    }

    private void validateContent() {
        if (contentBar.getText().trim().length() > 10000) {
            JOptionPane.showMessageDialog(null, "Mucho contenidooo!!!");
        }
    }

    private void createPost() {
        try {
            Post post = new Post();
            post.setTitle(titleBar.getText().trim());
            post.setText(contentBar.getText().trim());
            post.setAuthor(LoggedIn.getID());
            post.setPriv(privateCheckBox.isSelected());

            if (privateCheckBox.isSelected() && !allowedUsersForNewPost.isEmpty()) {
                // Convertir lista de User a lista de IDs
                ArrayList<Integer> allowedUserIds = new ArrayList<>();
                for (User user : allowedUsersForNewPost) {
                    allowedUserIds.add(user.getID());
                }
                post.setAllowedUsers(allowedUserIds);
            }

            boolean yay = ManejadorPublish.createPost(post, parentPost);

            if (yay) {
                showAlert("Éxito¡¡¡", "Post publicado correctamente");
                if (controller != null) controller.reload();
                closeWindow();
            } else {
                showAlert("Error!!!", "No se pudo publicar el post");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error!!!", "Error al publicar: " + e.getMessage());
        }
    }

    private void updatePost() {
        try {
            if (postParaEditar == null) return;

            postParaEditar.setTitle(titleBar.getText().trim());
            postParaEditar.setText(contentBar.getText().trim());
            postParaEditar.setPriv(privateCheckBox.isSelected());

            if (privateCheckBox.isSelected() && !allowedUsersForNewPost.isEmpty()) {
                // Convertir lista de User a lista de IDs
                ArrayList<Integer> allowedUserIds = new ArrayList<>();
                for (User user : allowedUsersForNewPost) {
                    allowedUserIds.add(user.getID());
                }
                postParaEditar.setAllowedUsers(allowedUserIds);
            } else {
                postParaEditar.setAllowedUsers(new ArrayList<>());
            }

            boolean success = ManejadorPublish.updatePost(postParaEditar);

            if (success) {
                showAlert("Éxito", "Post actualizado correctamente");
                closeWindow();
            } else {
                showAlert("Error", "No se pudo actualizar el post");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPostParaEditar(Post post) {
        this.postParaEditar = post;
        this.esEditable = true;

        if (post != null) {
            titleBar.setText(post.getTitle());
            contentBar.setText(post.getText());
            privateCheckBox.setSelected(post.isPriv());
            postButton.setText("Actualizar");

            if (post.isPriv()) {
                loadAllowedUsersForPost(post.getPostid());
                visibilityButton.setVisible(true);
            }
        }
    }

    private void loadAllowedUsersForPost(int postId) {
        try {
            allowedUsersForNewPost = ManejadorPublish.getAllowedUsersForPost(postId);
            updateSelectedUsersList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) postButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
