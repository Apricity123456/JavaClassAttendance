package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class AttendanceViewController {

    @FXML
    private TableView<?> attendanceTable;

    @FXML
    private TableColumn<?, ?> studentIDColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> attendanceStatusColumn;

    @FXML
    private Button markAttendanceButton;

    @FXML
    private void initialize() {
        // Initialization logic
        System.out.println("Attendance Management initialized.");
    }

    @FXML
    private void handleMarkAttendance() {
        // Logic to mark attendance
        System.out.println("Mark Attendance button clicked.");
    }
}
