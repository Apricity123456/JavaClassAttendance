package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> login());
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conn = DatabaseUtil.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");
            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("Login successful as " + role);
            } else {
                System.out.println("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
