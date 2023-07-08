package ru.shapovalov.furniture;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.sql.SQLException;

public class Admin {
    DataBase dataBase;
    Orders orders;
    Lines lines;
    Furniture furniture;
    Components component;
    Stores stores;

    public ListView<String> furnitureList;
    public ListView<String> componentList;
    public ListView<String> lineList;
    public ListView<String> shopList;
    public ListView<String> orderList;

    public Admin(DataBase dataBase) throws SQLException {
        this.dataBase = dataBase;

        // Создаем списки элементов
        furnitureList = new ListView<>();
        furniture = new Furniture(dataBase);
        ObservableList<String> furnitureItems = FXCollections.observableArrayList(
                furniture.getAll());
        furnitureList.setItems(furnitureItems);

        componentList = new ListView<>();
        component = new Components(dataBase);
        ObservableList<String> componentItems = FXCollections.observableArrayList(
                component.getAll());
        componentList.setItems(componentItems);

        lineList = new ListView<>();
        lines = new Lines(dataBase);
        ObservableList<String> listItems = FXCollections.observableArrayList(
                lines.getAll());
        lineList.setItems(listItems);

        shopList = new ListView<>();
        stores = new Stores(dataBase);
        ObservableList<String> shopItems = FXCollections.observableArrayList(
                stores.getAll());
        shopList.setItems(shopItems);

        orderList = new ListView<>();
        orders = new Orders(dataBase);
        ObservableList<String> orderItems = FXCollections.observableArrayList(
                 orders.getAll());
        orderList.setItems(orderItems);

    }

    public void addLine(String name) throws SQLException {
        lines.add(name);
    }

    public void deleteLine(int id) throws SQLException {
        lines.delete(id);
    }

    public void addFurniture(String lineName, String type, String article, double price, String[] comp){
        furniture.addFurniture(lineName, type, article, price, comp);
    }
    public void addOrder(String store,  String[] furniture) throws SQLException {
        orders.add(store, furniture);
    }



    public void deleteFurniture(int id) throws SQLException {
        furniture.delete(id);
    }

    public void addComponent(String type, double price) throws SQLException {
        component.add(type, price);
    }

    public void deleteComponent(int id) throws SQLException {
        component.delete(id);
    }

    public void addStore(String address, String fax) throws SQLException {
        stores.add(address, fax);
    }

    public void deleteStore(int id) throws SQLException {
        stores.delete(id);
    }

    public void refreshComponenteList() throws SQLException {
        componentList.getItems().clear();
        ObservableList<String> ComponentsItems = FXCollections.observableArrayList(
                component.getAll());
        componentList.setItems(ComponentsItems);
    }

    public void refreshFurnitureList() throws SQLException {
        furnitureList.getItems().clear();
        ObservableList<String> furnitureItems = FXCollections.observableArrayList(
                furniture.getAll());
        furnitureList.setItems(furnitureItems);
    }

    public void refreshOrderList() throws SQLException {
        orderList.getItems().clear();
        ObservableList<String> orderItems = FXCollections.observableArrayList(
                orders.getAll());
        orderList.setItems(orderItems);
    }

    public void refreshLineList() throws SQLException {
        lineList.getItems().clear();
        ObservableList<String> lineItems = FXCollections.observableArrayList(
                lines.getAll());
        lineList.setItems(lineItems);
    }

    public void refreshStoreList() throws SQLException {
        shopList.getItems().clear();
        ObservableList<String> storeItems = FXCollections.observableArrayList(
                stores.getAll());
        shopList.setItems(storeItems);
    }

    public void refreshOrdersList() throws SQLException {
        orderList.getItems().clear();
        ObservableList<String> orderItems = FXCollections.observableArrayList(
                orders.getAll());
        orderList.setItems(orderItems);
    }

    public DataBase getDataBase(){
        return this.dataBase;
    }

}