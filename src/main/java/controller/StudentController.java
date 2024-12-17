package controller;

import view.MainApp;
import javafx.fxml.FXML;

import java.io.IOException;

public class StudentController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleViewCourses() {
        try {
            mainApp.showCoursePage(); // 假设此方法会抛出 IOException
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Course Page.");
        }
    }

    @FXML
    private void handleLogOut() {
        try {
            mainApp.showLoginPage(); // 假设此方法会抛出 IOException
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Login Page.");
        }
    }
}
