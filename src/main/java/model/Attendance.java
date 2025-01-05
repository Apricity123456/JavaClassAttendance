package model;

import java.time.LocalDate;

public class Attendance {
    private int attendanceId;
    private int studentId;
    private String studentName;
    private String className;
    private LocalDate attendanceDate;
    private String status;
    private String remarks;

    public Attendance(int attendanceId, int studentId, String studentName, String className, LocalDate attendanceDate,
            String status, String remarks) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getClassName() {
        return className;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }
}
