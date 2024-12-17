package controller;
import view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminController {

    private MainApp mainApp;

    @FXML
    private Button manageStudentsButton;

    @FXML
    private Button manageCoursesButton;

    @FXML
    private Button logOutButton;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleManageStudents() throws IOException {
        mainApp.showStudentPage();
    }

    @FXML
    private void handleManageCourses() throws IOException {
        mainApp.showCoursePage();
    }

    @FXML
    private void handleLogOut() throws IOException {
        mainApp.showLoginPage();
    }
}
