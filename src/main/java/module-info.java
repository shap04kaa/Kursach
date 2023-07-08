module ru.shapovalov.furniture {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens ru.shapovalov.furniture to javafx.fxml;
    exports ru.shapovalov.furniture;
}