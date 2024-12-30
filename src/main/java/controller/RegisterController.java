package controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;

import javafx.collections.FXCollections;


import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import view.MainApp;
//import javafx.io.IOException;

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
//    @FXML
//    private ComboBox<String> roleComboBox;

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

        // 验证密码是否匹配
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        // 在这里执行注册逻辑，例如保存用户信息到数据库等
        System.out.println("Registration successful for " + name + " with username: " + username);
    }

    // 处理返回按钮点击事件
    @FXML
    private void handleBack() {
        mainApp.showLoginPage();
        System.out.println("Back to login page.");
    }
}
