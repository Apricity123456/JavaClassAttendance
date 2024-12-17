package view;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout; // 主布局

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Course Management");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        // 设置自定义窗口图标
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/background.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Failed to load window icon: " + e.getMessage());
        }

        // 主布局
        BorderPane root = new BorderPane();

        // 顶部标题栏和 Logo
        HBox headerBar = createHeaderBar();

        // 左侧导航栏
        VBox leftNavBar = createLeftNavBar();

        // 搜索栏、表格和操作按钮
        HBox searchBar = createSearchBar();
        TableView<String> tableView = createCentralTableView();
        HBox actionButtons = createActionButtons();

        // 中央内容区域
        VBox centerLayout = new VBox(10, searchBar, tableView, actionButtons);
        centerLayout.setPadding(new Insets(10));

        // 底部状态栏
        Label statusBar = new Label("Ready");
        statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px;");

        // 设置主布局的各个区域
        root.setTop(headerBar);
        root.setLeft(leftNavBar);
        root.setCenter(centerLayout);
        root.setBottom(statusBar);

        // 创建场景并显示
        Scene scene = new Scene(root, 900, 600);

        // 加载 CSS 样式表
        try {
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Failed to load CSS file: " + e.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * 创建顶部标题栏和 Logo
     */
    private HBox createHeaderBar() {
        ImageView logoImageView = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImageView.setImage(logo);
            logoImageView.setFitHeight(30); // 设置 Logo 高度
            logoImageView.setFitWidth(60);  // 设置 Logo 宽度
        } catch (Exception e) {
            System.out.println("Failed to load logo: " + e.getMessage());
        }

        // 标题文本
        Label title = new Label("Welcome to Course Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 关闭按钮
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");
        closeButton.setOnAction(e -> System.exit(0));

        // 标题栏布局
        HBox headerBar = new HBox(10, logoImageView, title, spacer, closeButton);
        headerBar.setPadding(new Insets(10));
        headerBar.setStyle("-fx-background-color: #ececec;");

        return headerBar;
    }


    // 顶部菜单栏
    private MenuBar createMenuBar() {
        Menu menuFile = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        menuFile.getItems().add(exitItem);

        Menu menuManage = new Menu("Manage");
        menuManage.getItems().addAll(new MenuItem("Manage Students"), new MenuItem("Manage Courses"));

        Menu menuHelp = new Menu("Help");
        menuHelp.getItems().add(new MenuItem("About"));

        return new MenuBar(menuFile, menuManage, menuHelp);
    }

    // 左侧导航栏
// 左侧导航栏函数
    private VBox createLeftNavBar() {
        VBox leftNav = new VBox(10);
        leftNav.setPadding(new Insets(10));
        leftNav.setStyle("-fx-background-color: #e6e6e6;");

        Label navTitle = new Label("Navigation");
        navTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button btnDashboard = new Button("Dashboard");
        Button btnStudents = new Button("Students");
        Button btnCourses = new Button("Courses");
        Button btnAttendance = new Button("Attendance");
        Button btnSettings = new Button("Settings");

        // 设置按钮最大宽度，以适应导航栏宽度
        btnDashboard.setMaxWidth(Double.MAX_VALUE);
        btnStudents.setMaxWidth(Double.MAX_VALUE);
        btnCourses.setMaxWidth(Double.MAX_VALUE);
        btnAttendance.setMaxWidth(Double.MAX_VALUE);
        btnSettings.setMaxWidth(Double.MAX_VALUE);

        // 添加按钮点击逻辑 (示例，页面跳转异常处理)
        btnDashboard.setOnAction(e -> handlePageException(() -> showAdminPage()));
        btnStudents.setOnAction(e -> handlePageException(() -> showStudentPage()));
        btnCourses.setOnAction(e -> handlePageException(() -> showCoursePage()));
        btnAttendance.setOnAction(e -> handlePageException(() -> showRegisterPage()));
        btnSettings.setOnAction(e -> handlePageException(() -> showLoginPage()));

        // 将所有按钮和标题加入导航栏
        leftNav.getChildren().addAll(navTitle, btnDashboard, btnStudents, btnCourses, btnAttendance, btnSettings);
        return leftNav;
    }

    private TableView<String> createCentralTableView() {
        TableView<String> tableView = new TableView<>();
        tableView.setPlaceholder(new Label("No Data Available"));
        tableView.getStyleClass().add("table-view");

        TableColumn<String, String> column1 = new TableColumn<>("Course ID");
        column1.setMinWidth(150);

        TableColumn<String, String> column2 = new TableColumn<>("Course Name");
        column2.setMinWidth(250);

        TableColumn<String, String> column3 = new TableColumn<>("Instructor");
        column3.setMinWidth(200);

        tableView.getColumns().addAll(column1, column2, column3);

        return tableView;
    }

    private HBox createActionButtons() {
        Button btnAdd = new Button("Add Course");
        btnAdd.getStyleClass().add("button-add");

        Button btnEdit = new Button("Edit Course");
        btnEdit.getStyleClass().add("button-edit");

        Button btnDelete = new Button("Delete Course");
        btnDelete.getStyleClass().add("button-delete");

        HBox actionButtons = new HBox(10, btnAdd, btnEdit, btnDelete);
        actionButtons.setPadding(new Insets(10, 0, 0, 0));
        return actionButtons;
    }

    private HBox createSearchBar() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search Course...");
        searchField.getStyleClass().add("text-field");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        searchButton.setOnAction(e -> System.out.println("Searching: " + searchField.getText()));

        HBox searchBar = new HBox(10, searchField, searchButton);
        searchBar.setPadding(new Insets(10));
        return searchBar;
    }



    // 页面跳转逻辑，抛出 IOException
    private void loadPage(String fxmlFilePath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        VBox page = loader.load();
        mainLayout.setCenter(page);
        primaryStage.setTitle(title);
    }

    // 页面跳转方法，抛出 IOException
    public void showLoginPage() throws IOException {
        loadPage("/view/Login.fxml", "Login Page");
    }

    public void showRegisterPage() throws IOException {
        loadPage("/view/RegisterView.fxml", "Register New User");
    }

    public void showAdminPage() throws IOException {
        loadPage("/view/AdminView.fxml", "Admin Dashboard");
    }

    public void showStudentPage() throws IOException {
        loadPage("/view/StudentView.fxml", "Student Management");
    }

    public void showCoursePage() throws IOException {
        loadPage("/view/CourseView.fxml", "Course Management");
    }

    // 异常处理方法：将 Checked 异常转为 Runtime 异常，避免麻烦
    private void handlePageException(PageLoader loader) {
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Page Load Error", "Failed to load the requested page.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FunctionalInterface
    interface PageLoader {
        void load() throws IOException;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
