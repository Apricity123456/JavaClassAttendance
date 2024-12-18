package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class GenerateReportController {

    @FXML
    private ChoiceBox<String> reportTypeChoiceBox;

    @FXML
    private Button generateReportButton;

    @FXML
    private void initialize() {
        // Initialization logic
        reportTypeChoiceBox.getItems().addAll("Attendance Report", "Class Report");
        System.out.println("Generate Report initialized.");
    }

    @FXML
    private void handleGenerateReport() {
        String selectedReport = reportTypeChoiceBox.getValue();
        System.out.println("Generate Report: " + selectedReport);
    }
}
