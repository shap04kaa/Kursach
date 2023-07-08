package ru.shapovalov.furniture;

import javafx.application.Application;
import javafx.stage.Stage;

public class FurnitureApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Auth auth = new Auth();
        AuthScene authScene = new AuthScene(auth);
        authScene.start(primaryStage);
        AuthController authController = new AuthController(auth, authScene);
    }
}
