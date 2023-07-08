package ru.shapovalov.furniture;

import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.sql.SQLException;

public class AuthController {
    Auth auth;
    AuthScene authScene;
    public AuthController(Auth auth, AuthScene authScene) {
        this.auth = auth;
        this.authScene = authScene;

        authScene.loginButton.setOnAction(event -> {
            String login = authScene.loginField.getText();
            String password = authScene.passwordField.getText();
            try {
                if (auth.getRole(login, password).equals("client")) {
                    Client client = null;
                try {
                    client = new Client(auth.dataBase);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ClientScene clientScene = new ClientScene(client);
                try {
                    clientScene.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ClientController clientController = new ClientController(client, clientScene);

                authScene.mainWindow.close();

                } else if (auth.getRole(login, password).equals("admin")){
                    Admin admin = new Admin(auth.dataBase);
                    AdminScene adminScene = new AdminScene(admin);
                    adminScene.start(new Stage());
                    AdminController adminController = new AdminController(admin, adminScene);

                    authScene.mainWindow.close();
                }else System.out.println("Ошибка добавлния");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        authScene.registrationButton.setOnAction(event -> {
            String username = authScene.newLoginField.getText();
            String password = authScene.newPasswordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка регистрации");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите логин и пароль");
                alert.showAndWait();
                return;
            }

            try {
                Client newClient = new Client(auth.dataBase, username, password);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Регистрация");
                alert.setHeaderText(null);
                alert.setContentText("Пользователь успешно зарегистрирован");
                alert.showAndWait();

                authScene.newLoginField.setText("");
                authScene.newPasswordField.setText("");

                ClientScene clientScene = new ClientScene(newClient);
                clientScene.start(new Stage());
                ClientController clientController = new ClientController(newClient, clientScene);
                authScene.mainWindow.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Вывод сообщения об ошибке
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка регистрации");
                alert.setHeaderText(null);
                alert.setContentText("Произошла ошибка при регистрации пользователя");
                alert.showAndWait();
            }
        });
    }

}
