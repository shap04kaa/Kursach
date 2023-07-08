package ru.shapovalov.furniture;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientScene extends Application  {
    Client client;

    public ClientScene(Client client){
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        TabPane tabPane = new TabPane();

        Tab lineTab = new Tab("Линии", client.lineList);
        Tab furnitureTab = new Tab("Мебель", client.furnitureList);
        Tab componentTab = new Tab("Компоненты", client.componentList);
        Tab shopTab = new Tab("Магазины", client.shopList);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // запретить закрытие вкладок

        tabPane.getTabs().addAll(lineTab, furnitureTab, componentTab, shopTab);
        furnitureTab.setClosable(false);

        root.getChildren().addAll(tabPane);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Furniture Factory");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
