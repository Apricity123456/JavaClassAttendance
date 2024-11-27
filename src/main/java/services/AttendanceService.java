package services;

import dao.AttendanceDAO;

public class AttendanceService {
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public void markAttendance(int studentId, int classId, boolean present) {
        try {
            attendanceDAO.markAttendance(studentId, classId, present);
            System.out.println("考勤已成功记录！");
        } catch (Exception e) {
            System.err.println("考勤记录失败: " + e.getMessage());
        }
    }
}