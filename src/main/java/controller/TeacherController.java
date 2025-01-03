package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import view.MainApp;

public class TeacherController {

    @FXML
    private TableView<?> tableView; // 课程表格占位符（这里暂时没有实际数据）

    @FXML
    private Button manageAttendanceButton;

    @FXML
    private Button manageClassesButton;

    @FXML
    private Button manageStudentsButton;

    @FXML
    private Button generateReportButton;

    @FXML
    private void initialize() {
        // 初始化页面逻辑，比如设置表格的数据
        System.out.println("Teacher Dashboard Initialized");
    }

    private MainApp mainApp;

    public void initializeMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleManageAttendance() {
        showInfoAlert("Manage Attendance", "This will open the attendance management page.");
        // TODO: 页面跳转逻辑，调用 MainApp.showAttendancePage()
    }

    @FXML
    private void handleManageClasses() {
        showInfoAlert("Manage Classes", "This will open the class management page.");
        // TODO: 页面跳转逻辑，调用 MainApp.showManageClassesPage()
    }

    @FXML
    private void handleManageStudents() {
        showInfoAlert("Manage Students", "This will open the student management page.");
        // TODO: 页面跳转逻辑，调用 MainApp.showManageStudentsPage()
    }

    @FXML
    private void handleGenerateReport() {
        showInfoAlert("Generate Report", "This will generate a report.");
        // TODO: 页面跳转逻辑，调用 MainApp.showGenerateReportPage()
    }

    @FXML
    private void handleLogout() {
        // 弹出确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        // 显式设置按钮
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                // 用户点击 OK，返回登录页面
                mainApp.showLoginPage();
            } else if (response == cancelButton) {
                // 用户点击 Cancel，什么都不做
                alert.close();
            }
        });
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
