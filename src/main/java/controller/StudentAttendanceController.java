package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Attendance;
import model.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class StudentAttendanceController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Attendance> attendanceTable;
    @FXML
    private TableColumn<Attendance, Integer> studentIdColumn;
    @FXML
    private TableColumn<Attendance, String> studentNameColumn;
    @FXML
    private TableColumn<Attendance, LocalDate> attendanceDateColumn;
    @FXML
    private TableColumn<Attendance, String> statusColumn;
    @FXML
    private TableColumn<Attendance, String> remarksColumn;

    private ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupAttendanceTable();
        loadAttendanceData();

        // 搜索事件监听器
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                searchAttendance();
            }
        });
    }

    private void setupAttendanceTable() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        attendanceDateColumn.setCellValueFactory(new PropertyValueFactory<>("attendanceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        remarksColumn.setCellValueFactory(new PropertyValueFactory<>("remarks"));
    }

    private void loadAttendanceData() {
        attendanceList.clear();
        String query = "SELECT a.attendance_id, s.student_id, s.student_name, a.attendance_date, a.status, a.remarks " +
                "FROM Attendance a " +
                "JOIN Students s ON a.student_id = s.student_id";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                attendanceList.add(new Attendance(
                        rs.getInt("attendance_id"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        "", // 添加缺少的 String 参数
                        rs.getDate("attendance_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("remarks")));
            }

            attendanceTable.setItems(attendanceList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load attendance data: " + e.getMessage());
        }
    }

    @FXML
    private void searchAttendance() {
        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            loadAttendanceData();
            return;
        }

        attendanceList.clear();
        String query = "SELECT a.attendance_id, s.student_id, s.student_name, a.attendance_date, a.status, a.remarks " +
                "FROM Attendance a " +
                "JOIN Students s ON a.student_id = s.student_id " +
                "WHERE s.student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Integer.parseInt(search));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                attendanceList.add(new Attendance(
                        rs.getInt("attendance_id"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        "", // 添加缺少的 String 参数
                        rs.getDate("attendance_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("remarks")));
            }

            attendanceTable.setItems(attendanceList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(
                getClass().getResourceAsStream("/images/logo.png"),
                60, 30, true, true);
        stage.getIcons().add(icon);

        alert.showAndWait();
    }
}
