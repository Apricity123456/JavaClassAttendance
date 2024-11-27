package dao;

import models.User;
import utils.DatabaseConnection;

import java.sql.*;

public class UserDAO {
    public User login(String username, String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            return user;
        }
        return null;
    }
}