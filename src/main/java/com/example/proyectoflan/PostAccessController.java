package com.example.proyectoflan;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PostAccessController implements Initializable {

    /*@FXML
    private TableView<User> allowancelistTable;

    @FXML
    private TableColumn<User, String> whitelistCloumn;

    @FXML
    private TableView<User> blacklistTable;

    @FXML
    private TableColumn<User, String> blacklistColumn;

    @FXML
    private Button confirmButton;

    private ObservableList<User> usuariosPermitidos = FXCollections.observableArrayList();
    private ObservableList<User> usuariosExcluidos = FXCollections.observableArrayList();

    private Post post;
    private User user;
    private Consumer<ArrayList<User>> allowedUsersCallback; // Callback para devolver los usuarios permitidos

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        // Configurar columnas
        if (whitelistCloumn != null) {
            whitelistCloumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            whitelistCloumn.setText("Usuarios Permitidos");
        }

        if (blacklistColumn != null) {
            blacklistColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            blacklistColumn.setText("Usuarios Excluidos");
        }
    }

    private void setupEventHandlers() {
        // Mover de excluidos a permitidos al hacer click
        blacklistTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                moveToAllowed(newSel);
            }
        });

        // Mover de permitidos a excluidos al hacer click
        allowancelistTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                moveToExcluded(newSel);
            }
        });
    }

    private void moveToAllowed(User user) {
        usuariosExcluidos.remove(user);
        usuariosPermitidos.add(user);
        refreshTables();
    }

    private void moveToExcluded(User user) {
        usuariosPermitidos.remove(user);
        usuariosExcluidos.add(user);
        refreshTables();
    }

    private void refreshTables() {
        allowancelistTable.setItems(usuariosPermitidos);
        blacklistTable.setItems(usuariosExcluidos);
        allowancelistTable.refresh();
        blacklistTable.refresh();
    }

    public void setPostData(Post post) {
        this.post = post;
        loadUsersData();
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    // Nuevo método para establecer el callback
    public void setAllowedUsersCallback(Consumer<ArrayList<User>> callback) {
        this.allowedUsersCallback = callback;
    }

    private void loadUsersData() {
        if (post == null) {
            // Si no hay post (nuevo post), cargar todos los usuarios excepto el actual
            loadAllUsersForNewPost();
            return;
        }

        try {
            // Obtener usuarios permitidos actualmente
            ArrayList<User> allowed = ManejadorPostAccess.getAllowedUsersForPost(post.getPostid());
            usuariosPermitidos.setAll(allowed);

            // Obtener usuarios excluidos
            ArrayList<User> excluded = ManejadorPostAccess.getExcludedUsersForPost(
                    post.getPostid(),
                    post.getUserID()
            );
            usuariosExcluidos.setAll(excluded);

            refreshTables();

        } catch (Exception e) {
            showAlert("Error", "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void loadAllUsersForNewPost() {
        try {
            // Para un nuevo post, cargar todos los usuarios excepto el autor
            ArrayList<User> allUsers = ManejadorPostAccess.getAllUsersExceptAuthor(LoggedIn.getID());

            // Inicialmente, todos los usuarios están excluidos
            usuariosExcluidos.setAll(allUsers);
            usuariosPermitidos.clear();

            refreshTables();

        } catch (Exception e) {
            showAlert("Error", "Error al cargar usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void saveAccessConfiguration() {
        try {
            // Convertir ObservableList a ArrayList
            ArrayList<User> allowedUsersList = new ArrayList<>(usuariosPermitidos);

            // Si hay un callback, llamarlo con los usuarios permitidos
            if (allowedUsersCallback != null) {
                allowedUsersCallback.accept(allowedUsersList);
            }

            // Si hay un post existente (edición), actualizar la base de datos
            if (post != null && post.getPostid() > 0) {
                boolean success = ManejadorPostAccess.updatePostVisibility(
                        post.getPostid(),
                        allowedUsersList
                );

                if (success) {
                    showAlert("Éxito", "Configuración guardada exitosamente");
                } else {
                    showAlert("Error", "No se pudo guardar la configuración");
                }
            } else {
                // Para nuevo post, solo mostrar mensaje
                showAlert("Éxito", "Configuración guardada (" + allowedUsersList.size() + " usuarios permitidos)");
            }

            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al guardar: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) (confirmButton != null ? confirmButton.getScene().getWindow() : null);
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/

    @FXML
    private TableView<User> allowancelistTable;

    @FXML
    private TableColumn<User, String> whitelistCloumn;

    @FXML
    private TableView<User> blacklistTable;

    @FXML
    private TableColumn<User, String> blacklistColumn;

    @FXML
    private Button confirmButton;

    @FXML
    private Button moveToAllowedButton;

    @FXML
    private Button moveToExcludedButton;

    private ObservableList<User> usuariosPermitidos = FXCollections.observableArrayList();
    private ObservableList<User> usuariosExcluidos = FXCollections.observableArrayList();

    private Post post;
    private User user;
    private Consumer<ArrayList<User>> allowedUsersCallback;

    private boolean isMoving = false;

    private Stage thisStage;
    public void getThisStage(Stage stage){
        thisStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupEventHandlers();
        setupButtons();
    }

    private void setupTableColumns() {
        if (whitelistCloumn != null) {
            whitelistCloumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            whitelistCloumn.setText("Usuarios Permitidos");
        }

        if (blacklistColumn != null) {
            blacklistColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            blacklistColumn.setText("Usuarios Excluidos");
        }
    }

    private void setupButtons() {
        if (moveToAllowedButton != null) {
            moveToAllowedButton.setOnAction(event -> {
                User selected = blacklistTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    moveToAllowed(selected);
                }
            });
        }

        if (moveToExcludedButton != null) {
            moveToExcludedButton.setOnAction(event -> {
                User selected = allowancelistTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    moveToExcluded(selected);
                }
            });
        }
    }

    private void setupEventHandlers() {
        allowancelistTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                User selected = allowancelistTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    moveToExcluded(selected);
                }
            }
        });

        blacklistTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                User selected = blacklistTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    moveToAllowed(selected);
                }
            }
        });
    }

    private void moveToAllowed(User user) {
        if (isMoving) return;

        try {
            isMoving = true;

            if (usuariosExcluidos.contains(user)) {
                usuariosExcluidos.remove(user);
                usuariosPermitidos.add(user);
                refreshTables();

                blacklistTable.getSelectionModel().clearSelection();
            }
        } finally {
            isMoving = false;
        }
    }

    private void moveToExcluded(User user) {
        if (isMoving) return;

        try {
            isMoving = true;

            if (usuariosPermitidos.contains(user)) {
                usuariosPermitidos.remove(user);
                usuariosExcluidos.add(user);
                refreshTables();

                allowancelistTable.getSelectionModel().clearSelection();
            }
        } finally {
            isMoving = false;
        }
    }

    private void refreshTables() {
        if (usuariosPermitidos.contains(LoggedIn.getYou())) usuariosPermitidos.remove(LoggedIn.getYou());
        if (usuariosExcluidos.contains(LoggedIn.getYou())) usuariosExcluidos.remove(LoggedIn.getYou());

        ObservableList<User> newPermitidos = FXCollections.observableArrayList(usuariosPermitidos);
        ObservableList<User> newExcluidos = FXCollections.observableArrayList(usuariosExcluidos);

        allowancelistTable.setItems(newPermitidos);
        blacklistTable.setItems(newExcluidos);
    }

    public void setPostData(Post post) {
        this.post = post;
        loadUsersData();
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public void setAllowedUsersCallback(Consumer<ArrayList<User>> callback) {
        this.allowedUsersCallback = callback;
    }

    private void loadUsersData() {
        usuariosPermitidos.clear();
        usuariosExcluidos.clear();

        if (post == null) {
            loadAllUsersForNewPost();
            return;
        }

        try {
            ArrayList<User> allowed = ManejadorPostAccess.getAllowedUsersForPost(post.getPostid());
            if (allowed != null) {
                usuariosPermitidos.addAll(allowed);
            }

            ArrayList<User> excluded = ManejadorPostAccess.getExcludedUsersForPost(
                    post.getPostid(),
                    post.getUserID()
            );
            if (excluded != null) {
                usuariosExcluidos.addAll(excluded);
            }

            refreshTables();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void loadAllUsersForNewPost() {
        try {
            ArrayList<User> allUsers = ManejadorPostAccess.getAllUsersExceptAuthor(LoggedIn.getID());

            if (allUsers != null) {
                usuariosExcluidos.addAll(allUsers);
                usuariosPermitidos.clear();
            }

            refreshTables();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al cargar usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void saveAccessConfiguration() {
        try {
            ArrayList<User> allowedUsersList = new ArrayList<>(usuariosPermitidos);
            allowedUsersList.add(LoggedIn.getYou());

            if (allowedUsersCallback != null) {
                allowedUsersCallback.accept(allowedUsersList);
            }

            if (post != null && post.getPostid() > 0) {
                boolean success = ManejadorPostAccess.updatePostVisibility(
                        post.getPostid(),
                        allowedUsersList
                );

                if (success) {
                    showAlert("Éxito", "Configuración guardada exitosamente");
                } else {
                    showAlert("Error", "No se pudo guardar la configuración");
                }
            } else {
                showAlert("Éxito", "Configuración guardada (" + allowedUsersList.size() + " usuarios permitidos)");
            }

            thisStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al guardar: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}