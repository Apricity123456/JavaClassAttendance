package controller;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import view.MainApp;
//import javafx.io.IOException;
import model.PasswordUtil;

public class RegisterController {

    @FXML
    private ComboBox<String> roleComboBox; // 用于选择身份
    @FXML
    private TextField nameField; // 姓名字段
    @FXML
    private TextField usernameField; // 用户名字段
    @FXML
    private PasswordField passwordField; // 密码字段
    @FXML
    private PasswordField confirmPasswordField; // 确认密码字段
    private MainApp mainApp;
    // @FXML
    // private ComboBox<String> roleComboBox;

    public void initialize() {
        // 使用代码动态设置ComboBox项
        roleComboBox.setItems(FXCollections.observableArrayList("Student", "Teacher"));
    }

    public void initializeMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // 处理身份选择变化事件
    @FXML
    private void handleRoleSelection() {
        String selectedRole = roleComboBox.getValue();

        // 根据选择的身份显示/隐藏姓名字段
        if (selectedRole != null) {
            if (selectedRole.equals("Student")) {
                nameField.setPromptText("Enter your full name");
            } else if (selectedRole.equals("Teacher")) {
                nameField.setPromptText("Enter your title and name");
            }
        }
    }

    // 处理注册按钮点击事件
    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match!");
            return;
        }

        // 哈希加密密码
        String hashedPassword = PasswordUtil.hashPassword(password);
        // 保存到数据库
        String query = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, hashedPassword);
            statement.setString(4, role);

            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to register user: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // 自定义图标并优化大小
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(
                getClass().getResourceAsStream("/images/logo.png"),
                60, 30, true, true // 设置宽度和高度，并保持长宽比
        );
        stage.getIcons().add(icon);

        alert.showAndWait();
    }

    // 处理返回按钮点击事件
    @FXML
    private void handleBack() {
        mainApp.showLoginPage();
        // System.out.println("Back to login page.");
    }
}
