package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import view.MainApp;

import model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.PasswordUtil;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ImageView logoImageView;

    private MainApp mainApp;

    @FXML
    public void initialize() {
        // 设置角色选项数据
        roleComboBox.setItems(FXCollections.observableArrayList("Student", "Teacher"));

        // 加载 Logo 图片
        try {
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.out.println("Error loading logo: " + e.getMessage());
        }
    }

    /**
     * 设置 MainApp 实例
     *
     * @param mainApp 主应用程序实例
     */
    public void initializeMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return;
        }
        String hashedPassword = PasswordUtil.hashPassword(password);

        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, role);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
                if ("Student".equals(role)) {
                    // 跳转到学生页面
                    mainApp.showStudentPage();
                } else if ("Teacher".equals(role)) {
                    // 跳转到教师页面
                    mainApp.showTeacherPage();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login: " + e.getMessage());
        }
    }

    // 这个方法将在点击注册链接时被调用
    @FXML
    private void showRegistrationPage() {
        mainApp.showRegistrationPage();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
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
}
