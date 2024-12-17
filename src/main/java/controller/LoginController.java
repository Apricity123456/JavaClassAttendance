package controller;
import view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private MainApp mainApp;

    // 注入 MainApp 实例
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() throws IOException {
        // 简单验证逻辑（实际应连接数据库）
        if ("admin".equals(usernameField.getText()) && "1234".equals(passwordField.getText())) {
            mainApp.showAdminPage(); // 登录成功，跳转到管理员页面
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        mainApp.showRegisterPage(); // 跳转到注册页面
    }
}
