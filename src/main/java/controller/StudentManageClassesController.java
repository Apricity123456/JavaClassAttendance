package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ClassModel;
import model.DatabaseConnection;

import java.sql.*;

public class StudentManageClassesController {

    @FXML
    private TextField searchField;
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

    private ObservableList<ClassModel> classList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupClassTable();
        loadClassData();

        // 搜索事件监听器
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                searchClasses();
            }
        });
    }

    private void setupClassTable() {
        classIdColumn.setCellValueFactory(new PropertyValueFactory<>("classId"));
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
    }

    private void loadClassData() {
        classList.clear();
        String query = "SELECT * FROM Classes";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                classList.add(new ClassModel(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getString("instructor"),
                        rs.getString("schedule")));
            }

            classTable.setItems(classList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchClasses() {
        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            loadClassData();
            return;
        }

        classList.clear();
        String query = "SELECT * FROM Classes WHERE LOWER(class_name) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + search + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                classList.add(new ClassModel(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getString("instructor"),
                        rs.getString("schedule")));
            }

            classTable.setItems(classList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
