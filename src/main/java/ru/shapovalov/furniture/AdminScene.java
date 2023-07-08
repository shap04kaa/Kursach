package ru.shapovalov.furniture;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminScene extends Application {
    public Stage primaryStage;
    public Admin admin;
    public Button addOrderButton;
    public Button addLineButton;
    public Button addFurnitureButton;
    public Button addComponentButton;
    public Button addShopButton;

    public Button deleteLine;
    public Button deleteFurniture;
    public Button deleteComponent;
    public Button deleteStore;
    public Tab orderTab;

    public AdminScene(Admin admin){
        this.admin = admin;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        AtomicReference<VBox> root = new AtomicReference<>(new VBox());
        root.get().setPadding(new Insets(10));
        root.get().setSpacing(10);

        TabPane tabPane = new TabPane();

        Tab lineTab = new Tab("Линии", admin.lineList);
        Tab furnitureTab = new Tab("Мебель", admin.furnitureList);
        Tab componentTab = new Tab("Компоненты", admin.componentList);
        Tab shopTab = new Tab("Магазины", admin.shopList);
        orderTab = new Tab("Все заказы", admin.orderList);

        addLineButton = new Button("Добавить новую линию");
        deleteLine = new Button("Удалить");

        lineTab.setContent(new VBox(admin.lineList, addLineButton, deleteLine));

        addFurnitureButton = new Button("Добавить новую мебель");
        deleteFurniture = new Button("Удалить");

        furnitureTab.setContent(new VBox(admin.furnitureList, addFurnitureButton, deleteFurniture));

        addComponentButton = new Button("Добавить новый компонент");
        deleteComponent = new Button("Удалить");

        componentTab.setContent(new VBox(admin.componentList, addComponentButton, deleteComponent));

        addShopButton = new Button("Добавить новый магазин");
        deleteStore = new Button("Удалить");

        shopTab.setContent(new VBox(admin.shopList, addShopButton, deleteStore));

        addOrderButton = new Button("Добавить новый заказ");
        orderTab.setContent(new VBox(admin.orderList, addOrderButton));

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // запретить закрытие вкладок

        tabPane.getTabs().addAll(lineTab, furnitureTab, componentTab, shopTab, orderTab);
        furnitureTab.setClosable(false);

        root.get().getChildren().addAll(tabPane);

        Scene scene = new Scene(root.get(), 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Furniture Factory");
        primaryStage.setResizable(false);

        primaryStage.show();

    }
}