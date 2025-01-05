package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ClassModel;
import model.DatabaseConnection;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.sql.*;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClassController {

    @FXML
    private TableView<ClassModel> classTable;
    @FXML
    private TableColumn<ClassModel, Integer> classIdColumn;
    @FXML
    private TableColumn<ClassModel, String> classNameColumn;
    @FXML
    private TableColumn<ClassModel, String> instructorColumn;
    @FXML
    private TableColumn<ClassModel, String> scheduleColumn;

    @FXML
    private TextField classNameField;
    @FXML
    private TextField instructorField;
    @FXML
    private TextField scheduleField;
    @FXML
    private TextField classSearchField;
    @FXML
    private Label errorLabel;

    private ObservableList<ClassModel> classList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        classIdColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getClassId()).asObject());
        classNameColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getClassName()));
        instructorColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getInstructor()));
        scheduleColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSchedule()));

        loadClassData();
    }

    private void loadClassData() {
        classTable.getItems().clear();
        String query = "SELECT * FROM classes";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                classTable.getItems().add(new ClassModel(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getString("instructor"),
                        rs.getString("schedule")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load classes.");
        }
    }

    // 搜索班级
    @FXML
    private void searchClass() {
        String search = classSearchField.getText().trim();

        // 如果搜索框为空，重新加载所有数据
        if (search.isEmpty()) {
            loadClassData();
            showAlert(Alert.AlertType.INFORMATION, "Search Cleared", "Showing all classes.");
            return;
        }

        // 清空当前列表
        classList.clear();

        // SQL 查询：模糊匹配 class_name
        String query = "SELECT * FROM classes WHERE LOWER(class_name) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + search + "%"); // 模糊匹配
            ResultSet rs = pstmt.executeQuery();

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                classList.add(new ClassModel(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getString("instructor"),
                        rs.getString("schedule")));
            }

            // 将结果绑定到表格
            classTable.setItems(classList);

            // 如果没有找到匹配结果
            if (!hasResults) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No classes found matching: " + search);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to search class.\nError: " + e.getMessage());
        }
    }

    @FXML
    private void addClass() {
        // 创建一个自定义对话框
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter Class Details:");

        // 使用 GridPane 布局
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // 添加输入控件
        TextField classNameField = new TextField();
        classNameField.setPromptText("Class Name");

        TextField instructorField = new TextField();
        instructorField.setPromptText("Instructor");

        TextField scheduleField = new TextField();
        scheduleField.setPromptText("e.g., Mon-Wed 10:00-11:30");

        grid.add(new Label("Class Name:"), 0, 0);
        grid.add(classNameField, 1, 0);
        grid.add(new Label("Instructor:"), 0, 1);
        grid.add(instructorField, 1, 1);
        grid.add(new Label("Schedule:"), 0, 2);
        grid.add(scheduleField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // 添加按钮
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // 处理用户输入
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String className = classNameField.getText().trim();
                String instructor = instructorField.getText().trim();
                String schedule = scheduleField.getText().trim();

                if (className.isEmpty() || instructor.isEmpty() || schedule.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Error", "All fields must be filled.");
                    return;
                }

                // 插入数据到数据库
                String query = "INSERT INTO classes (class_name, instructor, schedule) VALUES (?, ?, ?)";
                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, className);
                    pstmt.setString(2, instructor);
                    pstmt.setString(3, schedule);
                    pstmt.executeUpdate();

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Class added successfully!");
                    loadClassData();

                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add class.\n" + e.getMessage());
                }
            }
        });
    }

    // @FXML
    // private void updateClass() {
    // ClassModel selected = classTable.getSelectionModel().getSelectedItem();
    // if (selected == null) {
    // errorLabel.setText("Please select a class to update.");
    // return;
    // }
    //
    // String query = "UPDATE classes SET class_name=?, instructor=?, schedule=?
    // WHERE class_id=?";
    // try (Connection conn = DatabaseConnection.getConnection();
    // PreparedStatement pstmt = conn.prepareStatement(query)) {
    //
    // pstmt.setString(1, classNameField.getText());
    // pstmt.setString(2, instructorField.getText());
    // pstmt.setString(3, scheduleField.getText());
    // pstmt.setInt(4, selected.getClassId());
    // pstmt.executeUpdate();
    //
    // loadClassData();
    // clearFields();
    //
    // } catch (SQLException e) {
    // errorLabel.setText("Failed to update class.");
    // e.printStackTrace();
    // }
    // }

    @FXML
    private void deleteClass() {
        ClassModel selected = classTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Please select a class to delete.");
            return;
        }

        String query = "DELETE FROM classes WHERE class_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selected.getClassId());
            pstmt.executeUpdate();
            loadClassData();

        } catch (SQLException e) {
            errorLabel.setText("Failed to delete class.");
            e.printStackTrace();
        }
    }

    @FXML
    private void editClass() {
        ClassModel selectedClass = classTable.getSelectionModel().getSelectedItem();

        if (selectedClass == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a class to edit.");
            return;
        }

        // 创建自定义对话框
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Class");
        dialog.setHeaderText("Update Class Details:");

        // 使用 GridPane 布局
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // 添加输入控件并预填数据
        TextField classNameField = new TextField(selectedClass.getClassName());
        classNameField.setPromptText("Class Name");

        TextField instructorField = new TextField(selectedClass.getInstructor());
        instructorField.setPromptText("Instructor");

        TextField scheduleField = new TextField(selectedClass.getSchedule());
        scheduleField.setPromptText("e.g., Mon-Wed 10:00-11:30");

        grid.add(new Label("Class Name:"), 0, 0);
        grid.add(classNameField, 1, 0);
        grid.add(new Label("Instructor:"), 0, 1);
        grid.add(instructorField, 1, 1);
        grid.add(new Label("Schedule:"), 0, 2);
        grid.add(scheduleField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // 处理用户输入
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String className = classNameField.getText().trim();
                String instructor = instructorField.getText().trim();
                String schedule = scheduleField.getText().trim();

                if (className.isEmpty() || instructor.isEmpty() || schedule.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled.");
                    return;
                }

                // 更新数据库记录
                String query = "UPDATE classes SET class_name = ?, instructor = ?, schedule = ? WHERE class_id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, className);
                    pstmt.setString(2, instructor);
                    pstmt.setString(3, schedule);
                    pstmt.setInt(4, selectedClass.getClassId());
                    pstmt.executeUpdate();

                    loadClassData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Class updated successfully!");

                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update class.");
                }
            }
        });
    }

    @FXML
    private void handleSearchKeyPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            searchClass();
        }
    }

    private void clearFields() {
        classNameField.clear();
        instructorField.clear();
        scheduleField.clear();
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
