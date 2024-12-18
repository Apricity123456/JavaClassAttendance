package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;

public class ManageClassesController {

    @FXML
    private TableView<?> classesTable;

    @FXML
    private TableColumn<?, ?> classIDColumn;

    @FXML
    private TableColumn<?, ?> classNameColumn;

    @FXML
    private TableColumn<?, ?> instructorColumn;

    @FXML
    private Button addClassButton;

    @FXML
    private Button editClassButton;

    @FXML
    private Button deleteClassButton;

    @FXML
    private void initialize() {
        // Initialization logic
        System.out.println("Manage Classes initialized.");
    }

    @FXML
    private void handleAddClass() {
        System.out.println("Add Class button clicked.");
    }

    @FXML
    private void handleEditClass() {
        System.out.println("Edit Class button clicked.");
    }

    @FXML
    private void handleDeleteClass() {
        System.out.println("Delete Class button clicked.");
    }
}
