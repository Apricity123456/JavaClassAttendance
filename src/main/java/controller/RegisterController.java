package controller;
import view.MainApp;
import view.MainApp;
import javafx.fxml.FXML;

import java.io.IOException;

public class RegisterController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleRegister() {
        try {
            mainApp.showLoginPage(); // 返回登录页面
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading the login page.");
        }
    }
}
