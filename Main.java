import java.util.ArrayList;
import java.util.List;

// Base User Class
abstract class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

// Administrator Class
class Administrator extends User {
    private List<String> classes;
    private List<Student> students;

    public Administrator(String username, String password) {
        super(username, password);
        this.classes = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void manageClasses(String className, boolean add) {
        if (add) {
            classes.add(className);
            System.out.println("Class added: " + className);
        } else {
            classes.remove(className);
            System.out.println("Class removed: " + className);
        }
    }

    public void manageStudents(Student student, boolean add) {
        if (add) {
            students.add(student);
            System.out.println("Student added: " + student.getName());
        } else {
            students.remove(student);
            System.out.println("Student removed: " + student.getName());
        }
    }

    public void generateReport() {
        System.out.println("Generating report for all students...");
        for (Student student : students) {
            System.out.println("Student: " + student.getName() + ", Attendance: " + student.getAttendancePercentage() + "%");
        }
    }
}

// Teacher Class
class Teacher extends User {
    public Teacher(String username, String password) {
        super(username, password);
    }

    public void markAttendance(Student student, boolean present) {
        student.markAttendance(present);
        System.out.println("Attendance marked for " + student.getName() + ": " + (present ? "Present" : "Absent"));
    }
}

// Student Class
class Student {
    private String name;
    private int totalClasses;
    private int attendedClasses;

    public Student(String name) {
        this.name = name;
        this.totalClasses = 0;
        this.attendedClasses = 0;
    }

    public String getName() {
        return name;
    }

    public void markAttendance(boolean present) {
        totalClasses++;
        if (present) {
            attendedClasses++;
        }
    }

    public int getAttendancePercentage() {
        if (totalClasses == 0) return 0;
        return (attendedClasses * 100) / totalClasses;
    }
}

// Main Class (Entry Point)
public class Main {
    public static void main(String[] args) {
        // Create Administrator
        Administrator admin = new Administrator("admin", "password123");
        
        // Create Teacher
        Teacher teacher = new Teacher("teacher1", "teach123");
        
        // Create Students
        Student student1 = new Student("Alice");
        Student student2 = new Student("Bob");
        
        // Administrator adds students
        admin.manageStudents(student1, true);
        admin.manageStudents(student2, true);
        
        // Teacher marks attendance
        teacher.markAttendance(student1, true);
        teacher.markAttendance(student2, false);
        
        // Generate report
        admin.generateReport();
    }
}
