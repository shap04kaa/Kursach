package ru.shapovalov.furniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Lines {
    private DataBase dataBase;

    public Lines(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<String> getAll() throws SQLException {
        Statement statement = dataBase.connection.createStatement();

        String query = "SELECT l.id, l.name, GROUP_CONCAT(f.type SEPARATOR ', ') AS furniture_list " +
                "FROM `Lines` l LEFT JOIN Furniture f ON l.id = f.line_id " +
                "GROUP BY l.id, l.name;";
        ResultSet resultSet = statement.executeQuery(query);

        List<String> resultList = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String furnitureList = resultSet.getString("furniture_list");

            String row = "ID: " + id + ",  НАЗВАНИЕ: " + name + ",  МЕБЕЛЬ: " + furnitureList;
            resultList.add(row);
        }
        statement.close();

        return resultList;
    }

    public void add(String line) throws SQLException {
        Statement stmt = dataBase.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS id FROM `Lines`");
        int lineId = 0;
        while (rs.next()) {
            lineId = rs.getInt("id");
        }

        rs.close();
        stmt.close();

        lineId+=1;

        String query = "INSERT INTO `Lines` (id, name) " +
                "VALUES (" + lineId + ", \"" + line + "\"); ";
        PreparedStatement stmt2 = dataBase.connection.prepareStatement(query);
        stmt2.executeUpdate();
        stmt2.close();

    }


    public void delete(int id) throws SQLException {
        Statement statement = dataBase.connection.createStatement();
        String furnitureQuery = "DELETE FROM `Lines` WHERE id = \"" + id + "\"";
        statement.executeUpdate(furnitureQuery);
    }

}