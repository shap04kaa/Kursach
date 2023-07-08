package ru.shapovalov.furniture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Orders {
    private final DataBase dataBase;


    public Orders(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<String> getAll() throws SQLException {
        Statement statement = dataBase.connection.createStatement();

        String componentQuery = "SELECT o.id AS order_id, o.date, s.address, GROUP_CONCAT(CONCAT(f.type, ' (', of.quantity, ')') SEPARATOR ', ') AS furniture_list " +
                "FROM Orders o " +
                "JOIN Orders_Furniture of ON o.id = of.order_id " +
                "JOIN Stores s ON o.store_id = s.id " +
                "JOIN Furniture f ON of.furniture_id = f.id " +
                "GROUP BY o.id;";
        ResultSet resultSet = statement.executeQuery(componentQuery);

        List<String> tableRows = new ArrayList<>();

        while (resultSet.next()) {
            int orderId = resultSet.getInt("order_id");
            String date = resultSet.getString("date");
            String store = resultSet.getString("address");
            String furnitureList = resultSet.getString("furniture_list");

            String row = "ID: " + orderId + ", ДАТА: " + date + ", МАГАЗИН: " + store + ", КОМПОНЕНТЫ: " + furnitureList;
            tableRows.add(row);
        }
        statement.close();

        return tableRows;
    }

    public void add(String store, String[] components) throws SQLException {

        // Проверяем, существует ли указанная линия
        int storeId = getStoreId(dataBase.connection, store);
        if (storeId == -1) {
            System.out.println("Магазин с адресом " + store + " не найден");
            return;
        }

        try {
            // Генерируем уникальный идентификатор для новой мебели
            int orderId = getId();

            // Создаем запись о новой мебели в таблице Furniture
            String insertFurnitureQuery = "INSERT INTO Orders (id, date, store_id) VALUES (?, ?, ?)";
            PreparedStatement insertFurnitureStatement = dataBase.connection.prepareStatement(insertFurnitureQuery);
            insertFurnitureStatement.setInt(1, orderId);
            insertFurnitureStatement.setDate(2, new Date(System.currentTimeMillis()));
            insertFurnitureStatement.setInt(3, storeId);
            insertFurnitureStatement.executeUpdate();

            insertFurniture(dataBase.connection, orderId, components);

            System.out.println("Новая мебель успешно добавлена в базу данных");

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении мебели: " + e.getMessage());
        }
    }

    private static void insertFurniture(Connection connection, int orderId, String[] furniture ) throws SQLException {
        String query = "INSERT INTO Orders_Furniture (order_id, furniture_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (String f : furniture) {
                int furnitureId = getFurnitureId(connection, f.trim());
                statement.setInt(1, orderId);
                statement.setInt(2, furnitureId);
                statement.setInt(3, 1);
                statement.executeUpdate();
            }
        }
    }

    private static int getFurnitureId(Connection connection, String type) throws SQLException {
        String query = "SELECT id FROM Furniture WHERE type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        throw new SQLException("Component not found: " + type);
    }

    private static int getStoreId(Connection connection, String address) throws SQLException {
        String query = "SELECT id FROM Stores WHERE address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, address);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        throw new SQLException("Component not found: " + address);
    }

    private int getId() throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String furnitureQuery = "SELECT MAX(id) AS max_id FROM Orders;";
        ResultSet resultSet = statement.executeQuery(furnitureQuery);

        if (resultSet.next()) {
            return resultSet.getInt("max_id") + 1;
        }
        else return 0;
    }
}
