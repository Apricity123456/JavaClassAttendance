package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

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

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
