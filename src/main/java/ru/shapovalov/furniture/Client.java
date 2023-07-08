package ru.shapovalov.furniture;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
    DataBase dataBase;

    public ListView<String> furnitureList;
    public ListView<String> componentList;
    public ListView<String> lineList;
    public ListView<String> shopList;

    public Client(DataBase dataBase) throws SQLException {
        this.dataBase = dataBase;

        furnitureList = new ListView<>();
        Furniture furniture = new Furniture(dataBase);
        ObservableList<String> furnitureItems = FXCollections.observableArrayList(
                furniture.getAll());
        furnitureList.setItems(furnitureItems);

        componentList = new ListView<>();
        Components component = new Components(dataBase);
        ObservableList<String> componentItems = FXCollections.observableArrayList(
                component.getAll());
        componentList.setItems(componentItems);

        lineList = new ListView<>();
        Lines lines = new Lines(dataBase);
        ObservableList<String> orderItems = FXCollections.observableArrayList(
                lines.getAll());
        lineList.setItems(orderItems);

        shopList = new ListView<>();
        Stores stores = new Stores(dataBase);
        ObservableList<String> shopItems = FXCollections.observableArrayList(
                stores.getAll());
        shopList.setItems(shopItems);

    }

    public Client(DataBase dataBase, String login, String  password) throws SQLException {
        this.dataBase = dataBase;

        add(login, password);

        furnitureList = new ListView<>();
        Furniture furniture = new Furniture(dataBase);
        ObservableList<String> furnitureItems = FXCollections.observableArrayList(
                furniture.getAll());
        furnitureList.setItems(furnitureItems);

        componentList = new ListView<>();
        Components component = new Components(dataBase);
        ObservableList<String> componentItems = FXCollections.observableArrayList(
                component.getAll());
        componentList.setItems(componentItems);

        lineList = new ListView<>();
        Lines lines = new Lines(dataBase);
        ObservableList<String> orderItems = FXCollections.observableArrayList(
                lines.getAll());
        lineList.setItems(orderItems);

        shopList = new ListView<>();
        Stores stores = new Stores(dataBase);
        ObservableList<String> shopItems = FXCollections.observableArrayList(
                stores.getAll());
        shopList.setItems(shopItems);

    }



    private void add(String login, String password) throws SQLException {
        Statement stmt = dataBase.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS id FROM Users");
        int lineId = 0;
        while (rs.next()) {
            lineId = rs.getInt("id");
        }
        rs.close();
        stmt.close();

        lineId+=1;

        String query = "INSERT INTO Users (id, login, password, role) " +
                "VALUES (?, ?, ?, ?); ";
        try (PreparedStatement statement = dataBase.connection.prepareStatement(query)) {
            statement.setInt(1, lineId);
            statement.setString(2, login);
            statement.setString(3, password);
            statement.setString(4, "client");

            statement.executeUpdate();
        }

    }

     public DataBase getDataBase(){
        return this.dataBase;
     }
}
