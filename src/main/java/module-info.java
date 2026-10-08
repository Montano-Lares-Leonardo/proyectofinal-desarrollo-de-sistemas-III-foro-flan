module com.example.proyectoflan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires jdk.compiler;


    opens com.example.proyectoflan to javafx.fxml;
    exports com.example.proyectoflan;
}