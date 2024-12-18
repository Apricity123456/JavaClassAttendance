package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class ManageStudentsController {

    @FXML
    private TableView<?> studentsTable;

    @FXML
    private TableColumn<?, ?> studentIDColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> classColumn;

    @FXML
    private Button addStudentButton;

    @FXML
    private Button editStudentButton;

    @FXML
    private Button deleteStudentButton;

    @FXML
    private void initialize() {
        // Initialization logic
        System.out.println("Manage Students initialized.");
    }

    @FXML
    private void handleAddStudent() {
        System.out.println("Add Student button clicked.");
    }

    @FXML
    private void handleEditStudent() {
        System.out.println("Edit Student button clicked.");
    }

    @FXML
    private void handleDeleteStudent() {
        System.out.println("Delete Student button clicked.");
    }
}
