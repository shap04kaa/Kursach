package ru.shapovalov.furniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Stores {
    private final DataBase dataBase;

    public Stores(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<String> getAll() throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String componentQuery = "SELECT Stores.id, Stores.address, Stores.fax_number FROM Stores;";
        ResultSet resultSet = statement.executeQuery(componentQuery);

        List<String> storeList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String address = resultSet.getString("address");
            String faxNumber = resultSet.getString("fax_number");

            String storeInfo = "ID: " + id + ", Address: " + address + ", Fax Number: " + faxNumber;
            storeList.add(storeInfo);
        }

        statement.close();

        return storeList;
    }

    public void add( String address, String faxNumber) throws SQLException {
        int id = getId();
        String query = "INSERT INTO Stores (id, address, fax_number) VALUES (?, ?, ?)";
        try (PreparedStatement statement = dataBase.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, address);
            statement.setString(3, faxNumber);
            statement.executeUpdate();
        }
    }

    private int getId() throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String furnitureQuery = "SELECT MAX(id) AS max_id FROM Stores;";
        ResultSet resultSet = statement.executeQuery(furnitureQuery);
        if (resultSet.next()) {
            return resultSet.getInt("max_id") + 1;
        }
        else return 0;
    }


    public void delete(int id) throws SQLException {
        // Удаление связанных записей в таблице Orders_Furniture
        String furnitureQuery = "DELETE FROM Orders_Furniture WHERE order_id IN (SELECT id FROM Orders WHERE store_id = " + id + ")";
        Statement furnitureStatement = dataBase.connection.createStatement();
        furnitureStatement.executeUpdate(furnitureQuery);

        // Удаление записей в таблице Orders
        String ordersQuery = "DELETE FROM Orders WHERE store_id = " + id;
        Statement ordersStatement = dataBase.connection.createStatement();
        ordersStatement.executeUpdate(ordersQuery);

        // Удаление записи из таблицы Stores
        String storesQuery = "DELETE FROM Stores WHERE id = " + id;
        Statement storesStatement = dataBase.connection.createStatement();
        storesStatement.executeUpdate(storesQuery);
    }
}