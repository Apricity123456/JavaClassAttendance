package model;

public class ClassModel {
    private int classId;
    private String className;
    private String instructor;
    private String schedule;

    public ClassModel(int classId, String className, String instructor, String schedule) {
        this.classId = classId;
        this.className = className;
        this.instructor = instructor;
        this.schedule = schedule;
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getSchedule() {
        return schedule;
    }
}
