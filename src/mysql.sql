CREATE DATABASE ClassAttendance;

USE ClassAttendance;

-- 用户表
CREATE TABLE users (
    userID VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(50),
    role ENUM('admin', 'teacher', 'student') NOT NULL
);

-- 课程表
CREATE TABLE courses (
    courseID VARCHAR(50) PRIMARY KEY,
    courseName VARCHAR(100),
    teacherID VARCHAR(50),
    FOREIGN KEY (teacherID) REFERENCES users(userID)
);

-- 考勤记录表
CREATE TABLE attendance (
    recordID INT AUTO_INCREMENT PRIMARY KEY,
    courseID VARCHAR(50),
    studentID VARCHAR(50),
    status VARCHAR(10),
    date DATE,
    FOREIGN KEY (courseID) REFERENCES courses(courseID),
    FOREIGN KEY (studentID) REFERENCES users(userID)
);
