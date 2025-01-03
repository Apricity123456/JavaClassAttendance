package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import view.MainApp;

public class StudentController {

    @FXML
    private TableView<?> attendanceTableView; // 出勤记录表格占位符

    @FXML
    private Button viewAttendanceButton;

    @FXML
    private Button viewTimetableButton;

    @FXML
    private Button generateReportButton;

    @FXML
    private void initialize() {
        // 初始化页面逻辑，比如加载学生的考勤数据或课表数据
        System.out.println("Student Dashboard Initialized");
    }

    private MainApp mainApp;

    public void initializeMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleViewAttendance() {
        showInfoAlert("View Attendance", "This will display the attendance records.");
        // TODO: 加载出勤记录数据，更新 tableView
    }

    @FXML
    private void handleViewTimetable() {
        showInfoAlert("View Timetable", "This will display the timetable.");
        // TODO: 加载学生课表数据，更新 tableView
    }

    @FXML
    private void handleGenerateReport() {
        showInfoAlert("Generate Report", "This will generate your personal report.");
        // TODO: 生成报告的逻辑
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
