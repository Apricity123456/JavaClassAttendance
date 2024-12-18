package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

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

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
