package dao;

import models.Attendance;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceDAO {
    public void markAttendance(int studentId, int classId, boolean present) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO Attendance (student_id, class_id, date, present) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, studentId);
        ps.setInt(2, classId);
        ps.setDate(3, new java.sql.Date(new Date().getTime()));
        ps.setBoolean(4, present);
        ps.executeUpdate();
    }

    public List<Attendance> getAttendanceForClass(int classId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM Attendance WHERE class_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, classId);
        ResultSet rs = ps.executeQuery();

        List<Attendance> attendances = new ArrayList<>();
        while (rs.next()) {
            Attendance attendance = new Attendance();
            attendance.setId(rs.getInt("id"));
            attendance.setStudentId(rs.getInt("student_id"));
            attendance.setClassId(rs.getInt("class_id"));
            attendance.setDate(rs.getDate("date"));
            attendance.setPresent(rs.getBoolean("present"));
            attendances.add(attendance);
        }
        return attendances;
    }
}