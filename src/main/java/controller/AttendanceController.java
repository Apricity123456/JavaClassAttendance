package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Attendance;
import model.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class AttendanceController {

    @FXML
    private TableColumn<Attendance, Integer> studentIdColumn;
    @FXML
    private TableView<Attendance> attendanceTable;
    @FXML
    private TableColumn<Attendance, String> studentNameColumn;
    @FXML
    private TableColumn<Attendance, LocalDate> attendanceDateColumn;
    @FXML
    private TableColumn<Attendance, String> statusColumn;
    @FXML
    private TableColumn<Attendance, String> remarksColumn;
    @FXML
    private TextField attendanceSearchField;

    private ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        attendanceSearchField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    searchAttendance();
                    break;
                default:
                    break;
            }
        });

        // 设置列的属性映射
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        attendanceDateColumn.setCellValueFactory(new PropertyValueFactory<>("attendanceDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        remarksColumn.setCellValueFactory(new PropertyValueFactory<>("remarks"));

        loadAttendanceData();
    }

    private void loadAttendanceData() {
        attendanceList.clear();
        String query = "SELECT a.attendance_id, s.student_id, s.student_name, c.class_name, a.attendance_date, a.status, a.remarks "
                +
                "FROM Attendance a " +
                "JOIN Students s ON a.student_id = s.student_id " +
                "JOIN Classes c ON a.class_id = c.class_id";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                attendanceList.add(new Attendance(
                        rs.getInt("attendance_id"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("class_name"),
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
        String search = attendanceSearchField.getText().trim();

        if (search.isEmpty()) {
            loadAttendanceData(); // 如果为空，加载所有数据
            return;
        }

        attendanceList.clear();

        if (!search.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid numeric Student ID.");
            return;
        }

        String query = "SELECT a.attendance_id, s.student_id, s.student_name, c.class_name, a.attendance_date, a.status, a.remarks "
                +
                "FROM Attendance a " +
                "JOIN Students s ON a.student_id = s.student_id " +
                "JOIN Classes c ON a.class_id = c.class_id " +
                "WHERE s.student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Integer.parseInt(search));
            ResultSet rs = pstmt.executeQuery();

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                attendanceList.add(new Attendance(
                        rs.getInt("attendance_id"),
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("class_name"),
                        rs.getDate("attendance_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("remarks")));
            }

            attendanceTable.setItems(attendanceList);

            if (!hasResults) {
                showAlert(Alert.AlertType.INFORMATION, "No Results",
                        "No attendance records found for Student ID: " + search);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "An error occurred while searching the attendance records.\nError: " + e.getMessage());
        }
    }

    @FXML
    private void markPresent() {
        markAttendance("Present");
    }

    @FXML
    private void markAbsent() {
        markAttendance("Absent");
    }

    @FXML
    private void markLate() {
        markAttendance("Late");
    }

    private void markAttendance(String status) {
        Attendance selected = attendanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student record to update attendance.");
            return;
        }

        String query = "UPDATE attendance SET status = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, selected.getStudentId()); // 使用 studentId 而不是 id
            pstmt.executeUpdate();

            loadAttendanceData(); // 重新加载数据
            showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance status updated to: " + status);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Failed to update attendance status.\nError: " + e.getMessage());
        }
    }

    @FXML
    private void editRemarks() {
        Attendance selected = attendanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student record to edit remarks.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getRemarks());
        dialog.setTitle("Edit Remarks");
        dialog.setHeaderText("Edit remarks for " + selected.getStudentName());
        dialog.setContentText("Remarks:");

        dialog.showAndWait().ifPresent(newRemark -> {
            String query = "UPDATE Attendance SET remarks = ? WHERE attendance_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, newRemark);
                pstmt.setInt(2, selected.getAttendanceId());
                pstmt.executeUpdate();
                loadAttendanceData();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void updateAttendanceStatus() {
        Attendance selected = attendanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student record to update attendance.");
            return;
        }

        // 创建 ChoiceDialog 选择框
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Present", "Present", "Absent", "Late");
        dialog.setTitle("Update Attendance Status");
        dialog.setHeaderText("Update Attendance Status for " + selected.getStudentName());
        dialog.setContentText("Choose status:");

        dialog.showAndWait().ifPresent(status -> {
            String query = "UPDATE attendance SET status = ? WHERE student_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, status);
                pstmt.setInt(2, selected.getStudentId());
                pstmt.executeUpdate();

                loadAttendanceData(); // 重新加载数据
                showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance status updated to: " + status);

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Failed to update attendance status.\nError: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // 自定义图标并优化大小
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(
                getClass().getResourceAsStream("/images/logo.png"),
                60, 30, true, true // 设置宽度和高度，并保持长宽比
        );
        stage.getIcons().add(icon);

        alert.showAndWait();
    }
}
