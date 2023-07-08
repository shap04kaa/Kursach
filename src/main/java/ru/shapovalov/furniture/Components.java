package ru.shapovalov.furniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Components {
    private DataBase dataBase;

    public Components( DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<String> getAll() throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String conponentQuery = "SELECT c.id, c.type, c.cost " +
                "FROM Components c ";
        ResultSet resultSet = statement.executeQuery(conponentQuery);

        List<String> resultList = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String type = resultSet.getString("type");
            double cost = resultSet.getDouble("cost");

            String row = "ID: " + id + ", Type: " + type + ", Cost: " + cost;
            resultList.add(row);
        }

        statement.close();

        return resultList;
    }



    public void delete(int id) throws SQLException {
        // Удаление связанных записей из таблицы Furniture_Components
        String componentsQuery = "DELETE FROM Furniture_Components WHERE component_id = " + id;
        Statement componentsStatement = dataBase.connection.createStatement();
        componentsStatement.executeUpdate(componentsQuery);

        // Удаление записи из таблицы Furniture
        String furnitureQuery = "DELETE FROM Components WHERE id = " + id;
        Statement furnitureStatement = dataBase.connection.createStatement();
        furnitureStatement.executeUpdate(furnitureQuery);
    }

    public void add(String type, double price) throws SQLException {
        Statement stmt = dataBase.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS id FROM Components");
        int lineId = 0;
        while (rs.next()) {
            lineId = rs.getInt("id");
        }

        rs.close();
        stmt.close();

        lineId+=1;

        String query = "INSERT INTO Components (id, type, cost) " +
                "VALUES (" + lineId + ", \"" + type + "\", " + price + "); ";
        PreparedStatement stmt1 = dataBase.connection.prepareStatement(query);
        stmt1.executeUpdate();
        stmt1.close();
    }

}
