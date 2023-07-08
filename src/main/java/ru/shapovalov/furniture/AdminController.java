package ru.shapovalov.furniture;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class AdminController  {
    Admin admin;
    AdminScene adminScene;

    public AdminController(Admin admin, AdminScene adminScene){
        this.admin = admin;
        this.adminScene =  adminScene;

        adminScene.addOrderButton.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adminScene.primaryStage);
            VBox dialogVBox = new VBox();
            HBox dialogHBox = new HBox();
            dialogVBox.setPadding(new Insets(10));
            dialogVBox.setSpacing(10);

            Label labelabel = new Label("Магазин:");
            TextField textField = new TextField();

            Label furnitureLabel = new Label("Мебель:");
            TextField furnitureText = new TextField();

            Button addButton = new Button("Добавить");
            addButton.setOnAction(addEvent -> {
                String store = textField.getText();
                String[] furniture = furnitureText.getText().split(",");

                try {
                    admin.addOrder(store, furniture);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    admin.refreshOrderList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                dialog.close();
            });
            Button cancelButton = new Button("Отмена");
            cancelButton.setOnAction(cancelEvent -> dialog.close());

            dialogHBox.getChildren().addAll(addButton, cancelButton);
            dialogVBox.getChildren().addAll(labelabel, textField, furnitureLabel, furnitureText, dialogHBox);
            Scene dialogScene = new Scene(dialogVBox);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        });

        adminScene.addLineButton.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adminScene.primaryStage);
            VBox dialogVBox = new VBox();
            HBox dialogHBox = new HBox();
            dialogVBox.setPadding(new Insets(10));
            dialogVBox.setSpacing(10);

            Label newLineLabel = new Label("Новая линия:");
            TextField newLineText = new TextField();

            Button addButton = new Button("Добавить");
            addButton.setOnAction(addEvent -> {
                String name = newLineText.getText();
                try {
                    admin.addLine(name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    admin.refreshLineList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                dialog.close();
            });
            Button cancelButton = new Button("Отмена");
            cancelButton.setOnAction(cancelEvent -> dialog.close());
            dialogHBox.getChildren().addAll(addButton, cancelButton);
            dialogVBox.getChildren().addAll(newLineLabel, newLineText, dialogHBox);
            Scene dialogScene = new Scene(dialogVBox);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        });

        admin.lineList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            adminScene.deleteLine.setDisable(newValue == null);
        });

        adminScene.deleteLine.setOnAction(event -> {
            String selectedItem = admin.lineList.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem);
            if (selectedItem != null) {

                int startIndex = selectedItem.indexOf("ID: ") + 4;
                int endIndex = selectedItem.indexOf(",", startIndex);
                String extractedId = selectedItem.substring(startIndex, endIndex).trim();

                try {
                    admin.deleteLine(Integer.parseInt(extractedId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    admin.refreshLineList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        adminScene.addFurnitureButton.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adminScene.primaryStage);
            VBox dialogVBox = new VBox();
            HBox dialogHBox = new HBox();
            dialogVBox.setPadding(new Insets(10));
            dialogVBox.setSpacing(10);

            Label lineLabel = new Label("Линия:");
            TextField lineText = new TextField();

            Label typeLabel = new Label("Тип:");
            TextField typeText = new TextField();

            Label articleLabel = new Label("Артикул:");
            TextField articleText = new TextField();

            Label priceLabel = new Label("Цена:");
            TextField priceText = new TextField();

            Label componentLabel = new Label("Компоненты:");
            TextField componentText = new TextField();

            Button addButton = new Button("Добавить");
            addButton.setOnAction(addEvent -> {
                String line = lineText.getText();
                String type = typeText.getText();
                String article = articleText.getText();
                double price = Double.parseDouble(priceText.getText());
                String[] components = componentText.getText().split(",");

                admin.addFurniture(line, type, article, price, components);

                try {
                    admin.refreshFurnitureList();
                    admin.refreshLineList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                dialog.close();
            });
            Button cancelButton = new Button("Отмена");
            cancelButton.setOnAction(cancelEvent -> dialog.close());

            dialogHBox.getChildren().addAll(addButton, cancelButton);
            dialogVBox.getChildren().addAll(lineLabel, lineText, typeLabel, typeText,
                    articleLabel, articleText, priceLabel, priceText, componentLabel, componentText, dialogHBox);
            Scene dialogScene = new Scene(dialogVBox);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        });

        admin.furnitureList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            adminScene.deleteFurniture.setDisable(newValue == null);
        });

        adminScene.deleteFurniture.setOnAction(event -> {
            String selectedItem = admin.furnitureList.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem);
            if (selectedItem != null) {

                int startIndex = selectedItem.indexOf("ID: ") + 4;
                int endIndex = selectedItem.indexOf(",", startIndex);
                String extractedId = selectedItem.substring(startIndex, endIndex).trim();

                try {
                    admin.deleteFurniture(Integer.parseInt(extractedId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    admin.refreshFurnitureList();
                    admin.refreshOrderList();
                    admin.refreshComponenteList();
                    admin.refreshLineList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        adminScene.addComponentButton.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adminScene.primaryStage);
            VBox dialogVBox = new VBox();
            HBox dialogHBox = new HBox();
            dialogVBox.setPadding(new Insets(10));
            dialogVBox.setSpacing(10);

            Label componentLabel = new Label("Компонент:");
            TextField componentText = new TextField();

            Label priceLabel = new Label("Цена:");
            TextField priceText = new TextField();

            Button addButton = new Button("Добавить");
            addButton.setOnAction(addEvent -> {
                // Обработчик нажатия кнопки "Добавить"
                String type = componentText.getText();
                double price = Double.parseDouble(priceText.getText());
                try {
                    admin.addComponent(type, price);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    admin.refreshComponenteList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                dialog.close();
            });
            dialog.close();
            Button cancelButton = new Button("Отмена");
            cancelButton.setOnAction(cancelEvent -> dialog.close());

            dialogHBox.getChildren().addAll(addButton, cancelButton);
            dialogVBox.getChildren().addAll(componentLabel, componentText, priceLabel, priceText, dialogHBox);
            Scene dialogScene = new Scene(dialogVBox);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        });

        admin.componentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            adminScene.deleteComponent.setDisable(newValue == null);
        });
        adminScene.deleteComponent.setOnAction(event -> {
            String selectedItem = admin.componentList.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem);
            if (selectedItem != null) {
                int startIndex = selectedItem.indexOf("ID: ") + 4;
                int endIndex = selectedItem.indexOf(",", startIndex);
                String extractedId = selectedItem.substring(startIndex, endIndex).trim();

                try {
                    admin.deleteComponent(Integer.parseInt(extractedId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    admin.refreshComponenteList();
                    admin.refreshFurnitureList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        adminScene.addShopButton.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(adminScene.primaryStage);
            VBox dialogVBox = new VBox();
            HBox dialogHBox = new HBox();
            dialogVBox.setPadding(new Insets(10));
            dialogVBox.setSpacing(10);

            Label addressLabel = new Label("Адрес:");
            TextField addressText = new TextField();

            Label faxLabel = new Label("Номер факса:");
            TextField faxText = new TextField();

            Button addButton = new Button("Добавить");
            addButton.setOnAction(addEvent -> {
                String address = addressText.getText();
                String fax = faxText.getText();

                try {
                    admin.addStore(address, fax);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    admin.refreshStoreList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                dialog.close();
            });
            Button cancelButton = new Button("Отмена");
            cancelButton.setOnAction(cancelEvent -> dialog.close());

            dialogHBox.getChildren().addAll(addButton, cancelButton);
            dialogVBox.getChildren().addAll(addressLabel, addressText, faxLabel, faxText, dialogHBox);
            Scene dialogScene = new Scene(dialogVBox);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        });

        admin.shopList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            adminScene.deleteStore.setDisable(newValue == null);
        });
        adminScene.deleteStore.setOnAction(event -> {
            String selectedItem = admin.shopList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int startIndex = selectedItem.indexOf("ID: ") + 4;
                int endIndex = selectedItem.indexOf(",", startIndex);
                String extractedId = selectedItem.substring(startIndex, endIndex).trim();

                try {
                    admin.deleteStore(Integer.parseInt(extractedId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    admin.refreshStoreList();
                    admin.refreshOrderList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    /*private void showAddOrderDialog() {
        try {
            Stage dialog = new Stage();

            BorderPane root = new BorderPane();
            dialog.setScene(new Scene(root, 500, 400));

            Label title = new Label("Добавление нового заказа");
            title.setAlignment(Pos.CENTER);
            BorderPane.setAlignment(title, Pos.CENTER);
            BorderPane.setMargin(title, new Insets(20));
            root.setTop(title);

            Label labelabel = new Label("Магазин:");
            TextField textField = new TextField();


            ListView<CheckBox> furnitureCheckBoxList = new ListView<>();
            ObservableList<CheckBox> checkBoxList = furnitureCheckBoxList.getItems();

            Statement stmt = admin.getDataBase().connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, type, article FROM Furniture");

            while (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                String article = rs.getString(3);
                CheckBox checkBox = new CheckBox(String.format("%s [Артикул: %s]", type, article));
                checkBox.setId(Integer.toString(id));
                checkBoxList.add(checkBox);
            }

            rs.close();
            stmt.close();

            VBox checkboxBox = new VBox();
            checkboxBox.getChildren().addAll(labelabel, textField, furnitureCheckBoxList);

            //VBox checkboxBox = new VBox(furnitureCheckBoxList);
            checkboxBox.setAlignment(Pos.TOP_CENTER);
            checkboxBox.setPadding(new Insets(20));
            checkboxBox.setSpacing(10);
            root.setCenter(checkboxBox);

            Button addButton = new Button("Добавить заказ");
            addButton.setOnAction(event -> {
                try {
                    ObservableList<CheckBox> checkedItems = furnitureCheckBoxList.getItems();
                    StringBuilder furnitureIds = new StringBuilder();

                    for (CheckBox checkBox : checkedItems) {
                        if (checkBox.isSelected()) {
                            if (furnitureIds.length() > 0) {
                                furnitureIds.append(",");
                            }
                            furnitureIds.append(checkBox.getId());
                        }
                    }


                    PreparedStatement stmt1 = admin.getDataBase().connection.prepareStatement("SELECT id FROM Stores WHERE address = ?");
                    stmt1.setString(1, textField.getText().trim());
                    ResultSet rs1 = stmt1.executeQuery();
                    int storeId = 0;
                    while (rs1.next()) {
                        storeId = rs1.getInt("id");
                    }

                    rs1.close();
                    stmt1.close();

                    PreparedStatement stmt3 = admin.getDataBase().connection.prepareStatement("SELECT MAX(id) AS id FROM Orders");
                    ResultSet rs2 = stmt3.executeQuery();
                    int orderId = 0;
                    while (rs2.next()) {
                        orderId = rs2.getInt("id");
                    }

                    rs2.close();
                    stmt3.close();
                    orderId +=1;

                    PreparedStatement stmt2 = admin.getDataBase().connection.prepareStatement("INSERT INTO Orders (id , date, store_id) VALUES (?, ? , ?)");
                    stmt2.setInt(1, orderId);
                    stmt2.setDate(2, new Date(System.currentTimeMillis()));
                    stmt2.setInt(3, storeId);

                    stmt2.executeUpdate();
                    stmt2.close();

                    String[] furnitureIdList = furnitureIds.toString().split(",");
                    for (String furnitureId : furnitureIdList) {
                        PreparedStatement stmt4 = admin.getDataBase().connection.prepareStatement("INSERT INTO Orders_Furniture (order_id, furniture_id, quantity) VALUES (?, ?, ?)");
                        stmt4.setInt(1, orderId);
                        stmt4.setInt(2, Integer.parseInt(furnitureId));
                        stmt4.setInt(3, 1);
                        stmt4.executeUpdate();
                        stmt4.close();
                    }

                    adminScene.admin.refreshOrderList();

                    dialog.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            root.setBottom(addButton);
            BorderPane.setAlignment(addButton, Pos.CENTER);
            BorderPane.setMargin(addButton, new Insets(20));

            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
