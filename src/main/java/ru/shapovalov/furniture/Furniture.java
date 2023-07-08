package ru.shapovalov.furniture;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Furniture {
    private final DataBase dataBase;
    public Furniture( DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<String> getAll() throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String furnitureQuery = "SELECT f.id AS furniture_id, f.type, f.article, f.price, GROUP_CONCAT(c.type SEPARATOR ', ') AS components" +
                " FROM Furniture f JOIN Furniture_Components fc ON f.id = fc.furniture_id " +
                "JOIN Components c ON fc.component_id = c.id GROUP BY f.id;";
        ResultSet furnitureResult = statement.executeQuery(furnitureQuery);
        List<String> furnitureList = new ArrayList<>();

        while (furnitureResult.next()) {
            int furnitureId = furnitureResult.getInt("furniture_id");
            String type = furnitureResult.getString("type");
            String article = furnitureResult.getString("article");
            double price = furnitureResult.getDouble("price");
            String components = furnitureResult.getString("components");

            String row = "ID: " + furnitureId +
                    ", Type: " + type +
                    ", Article: " + article +
                    ", Price: " + price +
                    ", Components: " + components;

            furnitureList.add(row);
        }
        statement.close();

        return furnitureList;
    }

    public void addFurniture( String lineName, String type, String article, double price, String[] components) {

        // Проверяем, существует ли указанная линия
        int lineId = getLineIdByName(lineName);
        if (lineId == -1) {
            System.out.println("Линия с названием " + lineName + " не найдена");
            return;
        }

        try {
            // Генерируем уникальный идентификатор для новой мебели
            int furnitureId = getId();

            // Создаем запись о новой мебели в таблице Furniture
            String insertFurnitureQuery = "INSERT INTO Furniture (id, line_id, type, article, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertFurnitureStatement = dataBase.connection.prepareStatement(insertFurnitureQuery);
            insertFurnitureStatement.setInt(1, furnitureId);
            insertFurnitureStatement.setInt(2, lineId);
            insertFurnitureStatement.setString(3, type);
            insertFurnitureStatement.setString(4, article);
            insertFurnitureStatement.setDouble(5, price);
            insertFurnitureStatement.executeUpdate();

            insertComponents(dataBase.connection, furnitureId, components);

            System.out.println("Новая мебель успешно добавлена в базу данных");

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении мебели: " + e.getMessage());
        }
    }

    private static void insertComponents(Connection connection, int furnitureId, String[] components) throws SQLException {
        String query = "INSERT INTO Furniture_Components (furniture_id, component_id, quantity) VALUES (?, ?, 1)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (String component : components) {
                int componentId = getComponentId(connection, component.trim());
                statement.setInt(1, furnitureId);
                statement.setInt(2, componentId);
                statement.executeUpdate();
            }
        }
    }

    private static int getComponentId(Connection connection, String component) throws SQLException {
        String query = "SELECT id FROM Components WHERE type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, component);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        throw new SQLException("Component not found: " + component);
    }



    private int getLineIdByName(String lineName) {
        try {
            String selectLineQuery = "SELECT id FROM `Lines` WHERE name = ?";
            PreparedStatement selectLineStatement = dataBase.connection.prepareStatement(selectLineQuery);
            selectLineStatement.setString(1, lineName);
            ResultSet resultSet = selectLineStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
        }

        return -1; // Линия не найдена
    }

    private int getId() throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String furnitureQuery = "SELECT MAX(id) AS max_id FROM Furniture;";
        ResultSet resultSet = statement.executeQuery(furnitureQuery);

        if (resultSet.next()) {
            return resultSet.getInt("max_id") + 1;
        }
        else return 0;
    }
    public void delete(int id) throws SQLException {
        // Удаление связанных записей из таблицы Orders_Furniture
        String ordersQuery = "DELETE FROM Orders_Furniture WHERE furniture_id = " + id;
        Statement ordersStatement = dataBase.connection.createStatement();
        ordersStatement.executeUpdate(ordersQuery);

        // Удаление связанных записей из таблицы Furniture_Components
        String componentsQuery = "DELETE FROM Furniture_Components WHERE furniture_id = " + id;
        Statement componentsStatement = dataBase.connection.createStatement();
        componentsStatement.executeUpdate(componentsQuery);

        // Удаление записи из таблицы Furniture
        String furnitureQuery = "DELETE FROM Furniture WHERE id = " + id;
        Statement furnitureStatement = dataBase.connection.createStatement();
        furnitureStatement.executeUpdate(furnitureQuery);
    }
}
