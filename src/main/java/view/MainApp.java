package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.LoginController;
import controller.RegisterController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;        // 按钮控件
import javafx.scene.control.Label;         // 标签控件
import javafx.scene.image.ImageView;       // 图像视图，用于显示图标
import javafx.geometry.Insets;             // 内边距设置
import javafx.scene.input.MouseEvent;


import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private double xOffset = 0; // 鼠标X偏移量
    private double yOffset = 0; // 鼠标Y偏移量

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Course Management");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // 加载登录页面
        showLoginPage();
    }

    /**
     * 显示登录页面
     */
    public void showLoginPage() {
        try {
            // 加载 FXML 文件
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
//            VBox root = loader.load(); // 修改为 VBox，确保与 FXML 文件匹配
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            BorderPane root = loader.load();  // 假设 LoginView.fxml 使用的是 BorderPane

            // 调用 createHeaderBar() 并将其添加到顶部
            HBox headerBar = createHeaderBar("Login");
            root.setTop(headerBar); // 将标题栏添加到 BorderPane 的顶部
            // 创建场景
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

            // 添加窗口拖动功能
            addWindowDrag(scene);

            // 获取控制器并传递 MainApp 实例
            LoginController  Logincontroller = loader.getController();
            Logincontroller.initializeMainApp(this); // 确保调用的方法名为 initializeMainApp


            // 设置窗口图标和标题
            primaryStage.setScene(scene);
            primaryStage.setTitle("Course Management Login");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/background.png")));

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load Login Page.");
        }
    }


    /**
     * 显示教师主页面
     */
    public void showTeacherPage() {
            loadPage("/view/TeacherView.fxml","Teacher Dashboard");
    }

    public void showRegistrationPage() {
        try {
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterView.fxml"));
            BorderPane root = loader.load(); // 加载 BorderPane 布局

            // 调用 createHeaderBar() 并将其添加到顶部
            HBox headerBar = createHeaderBar("Register");
            root.setTop(headerBar); // 将标题栏添加到 BorderPane 的顶部

            // 创建 Scene，并将 root 作为根节点
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

            // 调用 addWindowDrag 并传入 Scene 实现窗口拖动
            addWindowDrag(scene);

            RegisterController RegisterController=loader.getController();
            RegisterController.initializeMainApp(this);
            // 设置场景和窗口标题
            primaryStage.setScene(scene);
            primaryStage.setTitle("Register");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load " + "Register");
        }

    }

    /**
     * 显示学生主页面
     */
    public void showStudentPage() {
            loadPage("/view/StudentView.fxml","Student Dashboard");
    }




    /**
     * 通用方法：加载页面
     */
    private void loadPage(String fxmlFilePath, String title) {
        try {
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            BorderPane root = loader.load(); // 加载 BorderPane 布局

            // 调用 createHeaderBar() 并将其添加到顶部
            HBox headerBar = createHeaderBar(title);
            root.setTop(headerBar); // 将标题栏添加到 BorderPane 的顶部

            // 创建 Scene，并将 root 作为根节点
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

            // 调用 addWindowDrag 并传入 Scene 实现窗口拖动
            addWindowDrag(scene);

            // 设置场景和窗口标题
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to load " + title);
        }
    }

    /**
     * 添加窗口拖动功能
     */
    private void addWindowDrag(Scene scene) {
        scene.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * 显示错误提示框
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private HBox createHeaderBar(String name) {
        // 左侧图标
        ImageView logoImageView = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImageView.setImage(logo);
            logoImageView.setFitHeight(30); // 设置图标高度
            logoImageView.setFitWidth(60);  // 设置图标宽度
        } catch (Exception e) {
            System.out.println("Failed to load logo: " + e.getMessage());
        }

        // 标题文本
        Label title = new Label("Welcome to Course Management - "+ name);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 右上角的退出按钮
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> System.exit(0)); // 退出程序

        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;"));

        // 标题栏布局
        HBox headerBar = new HBox(10, logoImageView, title, spacer, closeButton);
        headerBar.setPadding(new Insets(10));
        headerBar.setStyle("-fx-background-color: #ececec; -fx-border-color: #dcdcdc; -fx-border-width: 0 0 1 0;");

        return headerBar;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
