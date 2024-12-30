package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.MainApp;

//import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

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
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Please fill in all fields.");
        } else {
            // 登录成功后跳转到相应页面
            if ("Student".equals(role)) {
                mainApp.showStudentPage();
            } else if ("Teacher".equals(role)) {
                mainApp.showTeacherPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Role", "Please select a valid role.");
            }
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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


