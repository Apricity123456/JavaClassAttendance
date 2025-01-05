package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DatabaseConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.sql.SQLException;

public class GenerateReportStudentController {

    @FXML
    private ChoiceBox<String> reportTypeChoiceBox;

    @FXML
    public void initialize() {
        reportTypeChoiceBox.getItems().addAll("Attendance Report", "Class Report");
        reportTypeChoiceBox.setValue("Attendance Report");
    }

    @FXML
    private void generateReport() {
        String selectedReport = reportTypeChoiceBox.getValue();

        Alert formatAlert = new Alert(Alert.AlertType.CONFIRMATION);
        formatAlert.setTitle("Report Format");
        formatAlert.setHeaderText("Select report format");
        formatAlert.setContentText("Choose between PDF or Excel format.");

        ButtonType pdfButton = new ButtonType("PDF");
        ButtonType excelButton = new ButtonType("Excel");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        formatAlert.getButtonTypes().setAll(pdfButton, excelButton, cancelButton);

        formatAlert.showAndWait().ifPresent(response -> {
            if (response == pdfButton) {
                exportReportToPDF(selectedReport);
            } else if (response == excelButton) {
                exportReportToExcel(selectedReport);
            }
        });
    }

    private void exportReportToPDF(String reportType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save " + reportType + " PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement()) {

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                document.add(new Paragraph(reportType, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
                document.add(new Paragraph(" ")); // 空行

                PdfPTable table = null;

                if ("Attendance Report".equals(reportType)) {
                    table = new PdfPTable(7);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[] { 1, 2, 2, 2, 2, 2, 3 });
                    addTableHeader(table, "Attendance Report");
                } else if ("Class Report".equals(reportType)) {
                    table = new PdfPTable(4);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[] { 1, 3, 3, 3 });
                    addTableHeader(table, "Class Report");
                }

                String query = getReportQuery(reportType);
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    if ("Attendance Report".equals(reportType)) {
                        table.addCell(String.valueOf(rs.getInt("attendance_id")));
                        table.addCell(rs.getString("student_name"));
                        table.addCell(String.valueOf(rs.getInt("student_id")));
                        table.addCell(rs.getString("class_name"));
                        table.addCell(rs.getString("attendance_date"));
                        table.addCell(rs.getString("status"));
                        table.addCell(rs.getString("remarks"));
                    } else if ("Class Report".equals(reportType)) {
                        table.addCell(String.valueOf(rs.getInt("class_id")));
                        table.addCell(rs.getString("class_name"));
                        table.addCell(rs.getString("instructor"));
                        table.addCell(rs.getString("schedule"));
                    }
                }

                document.add(table);
                document.close();

                showAlert(Alert.AlertType.INFORMATION, "Success", reportType + " PDF generated!");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate PDF: " + e.getMessage());
            }
        }
    }

    private void exportReportToExcel(String reportType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save " + reportType + " Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    Workbook workbook = new XSSFWorkbook()) {

                Sheet sheet = workbook.createSheet(reportType);

                // 添加表头
                Row header = sheet.createRow(0);
                if ("Attendance Report".equals(reportType)) {
                    addExcelHeader(header, "ID", "Name", "Student ID", "Course Name", "Date", "Status", "Remark");
                } else if ("Class Report".equals(reportType)) {
                    addExcelHeader(header, "Class ID", "Class Name", "Instructor", "Schedule");
                }

                // 填充数据
                String query = getReportQuery(reportType);
                ResultSet rs = stmt.executeQuery(query);

                int rowIndex = 1;
                while (rs.next()) {
                    Row row = sheet.createRow(rowIndex++);
                    if ("Attendance Report".equals(reportType)) {
                        row.createCell(0).setCellValue(rs.getInt("attendance_id"));
                        row.createCell(1).setCellValue(rs.getString("student_name"));
                        row.createCell(2).setCellValue(rs.getInt("student_id"));
                        row.createCell(3).setCellValue(rs.getString("class_name"));
                        row.createCell(4).setCellValue(rs.getString("attendance_date"));
                        row.createCell(5).setCellValue(rs.getString("status"));
                        row.createCell(6).setCellValue(rs.getString("remarks"));
                    } else if ("Class Report".equals(reportType)) {
                        row.createCell(0).setCellValue(rs.getInt("class_id"));
                        row.createCell(1).setCellValue(rs.getString("class_name"));
                        row.createCell(2).setCellValue(rs.getString("instructor"));
                        row.createCell(3).setCellValue(rs.getString("schedule"));
                    }
                }

                // 调整列宽
                for (int i = 0; i < header.getLastCellNum(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // 保存 Excel 文件
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", reportType + " Excel report generated!");

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch data: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate Excel report: " + e.getMessage());
            }
        }
    }

    private String getReportQuery(String reportType) {
        return switch (reportType) {
            case "Attendance Report" ->
                """
                            SELECT a.attendance_id, s.student_name, a.student_id, c.class_name, a.attendance_date, a.status, a.remarks
                            FROM attendance a
                            JOIN students s ON a.student_id = s.student_id
                            JOIN classes c ON a.class_id = c.class_id
                        """;
            case "Class Report" -> "SELECT class_id, class_name, instructor, schedule FROM classes";
            default -> "";
        };
    }

    private void addTableHeader(PdfPTable table, String reportType) {
        if ("Attendance Report".equals(reportType)) {
            addPdfTableHeader(table, "ID", "Name", "Student ID", "Course Name", "Date", "Status", "Remark");
        } else if ("Class Report".equals(reportType)) {
            addPdfTableHeader(table, "Class ID", "Class Name", "Instructor", "Schedule");
        }
    }

    private void addPdfTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addExcelHeader(Row row, String... headers) {
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
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
