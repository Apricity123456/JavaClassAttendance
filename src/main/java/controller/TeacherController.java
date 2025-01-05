package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.scene.layout.Pane;

public class TeacherController {

    @FXML
    private VBox defaultView;
    @FXML
    private VBox attendanceView;
    @FXML
    private VBox classesView;
    @FXML
    private VBox studentsView;
    @FXML
    private VBox reportView;
    @FXML
    private Pane contentPane;
    @FXML
    private Button manageStudentsButton;

    @FXML
    public void initialize() {
        System.out.println(getClass().getResource("/view/ManageStudents.fxml"));

        // 显示默认视图
        showDefaultView();
    }

    private void hideAllViews() {
        defaultView.setVisible(false);
        attendanceView.setVisible(false);
        classesView.setVisible(false);
        studentsView.setVisible(false);
        reportView.setVisible(false);
    }

    @FXML
    private void showDefaultView() {
        hideAllViews();
        defaultView.setVisible(true);
    }

    @FXML
    private void showAttendanceView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AttendanceView.fxml"));
            Parent root = loader.load();
            contentPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showClassesView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ManageClasses.fxml"));
            contentPane.getChildren().setAll(root); // contentPane 是你右侧的主要区域容器
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Manage Classes view.");
        }
    }

    @FXML
    private void showStudentsView() {
        try {
            System.out.println(getClass().getResource("/view/ManageStudents.fxml"));
            // 加载 ManageStudents.fxml 文件
            Parent root = FXMLLoader.load(getClass().getResource("/view/ManageStudents.fxml"));

            // 将加载的视图设置到 contentPane 中
            contentPane.getChildren().setAll(root);
        } catch (IOException e) {
            // 捕获并打印异常信息
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Manage Students view.");
        }
    }

    @FXML
    private void showReportView() {
        try {
            // 加载 GenerateReport.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/view/GenerateReport.fxml"));
            contentPane.getChildren().setAll(root); // contentPane 是你的右侧主容器
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Generate Report view.");
        }
    }

    private MainApp mainApp;

    public void initializeMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogout() {
        // 弹出确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        // 显式设置按钮
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                // 用户点击 OK，返回登录页面
                mainApp.showLoginPage();
            } else if (response == cancelButton) {
                // 用户点击 Cancel，什么都不做
                alert.close();
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
