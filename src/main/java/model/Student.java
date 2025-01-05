package model;

import javafx.beans.property.*;

public class Student {
    private final IntegerProperty studentId;
    private final StringProperty studentName;
    private final StringProperty studentEmail;
    private final StringProperty studentPhone;
    private final StringProperty studentAddress; // 新增 Address 属性
    private final IntegerProperty studentClassId;

    // 修改构造函数，加入 address 字段
    public Student(int id, String name, String email, String phone, String address, int classId) {
        this.studentId = new SimpleIntegerProperty(id);
        this.studentName = new SimpleStringProperty(name);
        this.studentEmail = new SimpleStringProperty(email);
        this.studentPhone = new SimpleStringProperty(phone);
        this.studentAddress = new SimpleStringProperty(address); // 初始化 Address
        this.studentClassId = new SimpleIntegerProperty(classId);
    }

    // --- 属性绑定方法 ---
    public IntegerProperty studentIdProperty() {
        return studentId;
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public StringProperty studentEmailProperty() {
        return studentEmail;
    }

    public StringProperty studentPhoneProperty() {
        return studentPhone;
    }

    public StringProperty studentAddressProperty() {
        return studentAddress;
    } // 新增

    public IntegerProperty studentClassIdProperty() {
        return studentClassId;
    }

    // --- Getter 方法 ---
    public int getStudentId() {
        return studentId.get();
    }

    public String getStudentName() {
        return studentName.get();
    }

    public String getStudentEmail() {
        return studentEmail.get();
    }

    public String getStudentPhone() {
        return studentPhone.get();
    }

    public String getStudentAddress() {
        return studentAddress.get();
    } // 新增

    public int getStudentClassId() {
        return studentClassId.get();
    }

    // --- Setter 方法 ---
    public void setStudentName(String name) {
        this.studentName.set(name);
    }

    public void setStudentEmail(String email) {
        this.studentEmail.set(email);
    }

    public void setStudentPhone(String phone) {
        this.studentPhone.set(phone);
    }

    public void setStudentAddress(String address) {
        this.studentAddress.set(address);
    } // 新增

    public void setStudentClassId(int classId) {
        this.studentClassId.set(classId);
    }
}
