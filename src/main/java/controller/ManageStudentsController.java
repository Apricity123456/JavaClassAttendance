package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Student;
import model.DatabaseConnection;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.*;

public class ManageStudentsController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> studentIdColumn;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String> studentEmailColumn;
    @FXML
    private TableColumn<Student, String> studentPhoneColumn;
    @FXML
    private TableColumn<Student, String> studentAddressColumn;

    @FXML
    private TableColumn<Student, Integer> studentClassIdColumn;

    @FXML
    private TextField studentSearchField;
    @FXML
    private Label errorLabel;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    /**
     * 初始化表格列和数据
     */
    @FXML
    public void initialize() {
        studentIdColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStudentId()).asObject());
        studentNameColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentName()));
        studentEmailColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentEmail()));
        studentPhoneColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentPhone()));
        studentAddressColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentAddress())); // 新增绑定
        studentClassIdColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStudentClassId())
                        .asObject());

        loadStudentData();

        studentSearchField.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                searchStudent();
            }
        });
    }

    /**
     * 加载学生数据
     */
    private void loadStudentData() {
        studentTable.getItems().clear();
        String query = "SELECT student_id, student_name, email, phone, address, class_id FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                studentTable.getItems().add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"), // 新增映射
                        rs.getInt("class_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load students.");
        }
    }

    /**
     * 搜索学生
     */
    @FXML
    private void searchStudent() {
        String search = studentSearchField.getText().trim();

        if (search.isEmpty()) {
            loadStudentData();
            showAlert(Alert.AlertType.INFORMATION, "Search Cleared", "Showing all students.");
            return;
        }

        studentList.clear();

        String query = "SELECT student_id, student_name, email, phone, address, class_id FROM students WHERE LOWER(student_name) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + search + "%");
            ResultSet rs = pstmt.executeQuery();

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                studentList.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"), // 新增映射
                        rs.getInt("class_id")));
            }

            studentTable.setItems(studentList);

            if (!hasResults) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No students found matching: " + search);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to search students.");
        }
    }

    /**
     * 添加学生
     */
    @FXML
    private void addStudent() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter Student Details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        TextField classIdField = new TextField();
        classIdField.setPromptText("Class ID");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new Label("Class ID:"), 0, 3);
        grid.add(classIdField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                int classId = Integer.parseInt(classIdField.getText().trim());

                String query = "INSERT INTO students (student_name, email, phone, address, class_id) VALUES (?, ?, ?, ?, ?)";

                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, phone);
                    pstmt.setString(4, address);
                    pstmt.setInt(5, classId);
                    pstmt.executeUpdate();

                    loadStudentData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student.");
                }
            }
        });
    }

    @FXML
    private void editStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to edit.");
            return;
        }

        // 创建一个自定义对话框
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");
        dialog.setHeaderText("Update Student Details:");

        // 使用 GridPane 布局
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // 添加输入控件并预填数据
        TextField nameField = new TextField(selectedStudent.getStudentName());
        nameField.setPromptText("Name");

        TextField emailField = new TextField(selectedStudent.getStudentEmail());
        emailField.setPromptText("Email");

        TextField phoneField = new TextField(selectedStudent.getStudentPhone());
        phoneField.setPromptText("Phone");

        TextField addressField = new TextField(selectedStudent.getStudentAddress());
        addressField.setPromptText("Address");

        TextField classIdField = new TextField(String.valueOf(selectedStudent.getStudentClassId()));
        classIdField.setPromptText("Class ID");

        // 按正确顺序添加到 GridPane
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);

        grid.add(new Label("Class ID:"), 0, 3);
        grid.add(classIdField, 1, 3);

        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // 处理用户输入
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                int classId;

                try {
                    classId = Integer.parseInt(classIdField.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Class ID must be a number.");
                    return;
                }

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled.");
                    return;
                }

                // 更新数据库记录
                String query = "UPDATE students SET student_name = ?, email = ?, phone = ?, address = ?, class_id = ? WHERE student_id = ?";

                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, phone);
                    pstmt.setString(4, address);
                    pstmt.setInt(5, classId);
                    pstmt.setInt(6, selectedStudent.getStudentId());

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        loadStudentData();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Update Failed",
                                "No rows were updated. Check the student ID.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update student: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void deleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Student");
        confirmationAlert.setHeaderText("Are you sure you want to delete this student?");
        confirmationAlert.setContentText("Student: " + selectedStudent.getStudentName());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String query = "DELETE FROM students WHERE student_id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setInt(1, selectedStudent.getStudentId());
                    pstmt.executeUpdate();

                    loadStudentData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete student.");
                }
            }
        });
    }

    private void clearFields() {
        studentSearchField.clear();
    }

    /**
     * 按键搜索
     */
    @FXML
    private void handleSearchKeyPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            searchStudent();
        }
    }

    /**
     * 显示消息框
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // 自定义图标（可选）
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

        alert.showAndWait();
    }

}
