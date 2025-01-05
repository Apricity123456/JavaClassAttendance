package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.MainApp;
import java.io.IOException;

public class StudentDashboardController {
    @FXML
    private VBox defaultView;
    @FXML
    private VBox attendanceView;
    @FXML
    private VBox classesView;
    @FXML
    private VBox reportView;
    @FXML
    private Pane contentPane;

    private MainApp mainApp;

    @FXML
    public void initialize() {
        showAttendanceView(); // 默认显示考勤视图
    }

    private void hideAllViews() {
        defaultView.setVisible(false);
        attendanceView.setVisible(false);
        classesView.setVisible(false);
        reportView.setVisible(false);
    }

    /**
     * 显示考勤视图
     */
    @FXML
    private void showAttendanceView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentAttendanceView.fxml"));
            Parent root = loader.load();
            contentPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Attendance view.");
        }
    }

    @FXML
    private void showClassesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentManageClasses.fxml"));
            Parent root = loader.load();
            contentPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Classes view.");
        }
    }

    /**
     * 显示报告生成视图
     */
    @FXML
    private void showReportView() {
        try {
            // 加载 GenerateReport.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/view/GenerateReportStudent.fxml"));
            contentPane.getChildren().setAll(root); // contentPane 是你的右侧主容器
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Generate Report view.");
        }
    }

    /**
     * 退出登录
     */
    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                mainApp.showLoginPage();
            } else if (response == cancelButton) {
                alert.close();
            }
        });
    }

    public void initializeMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
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
