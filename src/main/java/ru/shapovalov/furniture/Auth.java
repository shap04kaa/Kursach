package ru.shapovalov.furniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Auth {
    DataBase dataBase;
    public Auth(){
        this.dataBase = new DataBase();
    }
    public String getRole(String login, String password) throws SQLException {
        String query = "SELECT role " +
                "FROM Users u " +
                "WHERE u.login = \""+ login +"\" AND u.password = \""+ password +"\"";
        PreparedStatement statement = dataBase.connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String role = resultSet.getString("role");
            resultSet.close();
            statement.close();
            return role;
        } else {
            throw new SQLException("No rows found");
        }
    }

}
