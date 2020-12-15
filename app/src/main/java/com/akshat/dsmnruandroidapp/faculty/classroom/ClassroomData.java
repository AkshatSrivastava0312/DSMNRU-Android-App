package com.akshat.dsmnruandroidapp.faculty.classroom;

public class ClassroomData {

    private  String uniqueKey, className, classroomCreationDate, classroomCreationTime, classroomAdmin;

    public ClassroomData(String uniqueKey, String className, String classroomCreationDate, String classroomCreationTime, String classroomAdmin) {
        this.uniqueKey = uniqueKey;
        this.className = className;
        this.classroomCreationDate = classroomCreationDate;
        this.classroomCreationTime = classroomCreationTime;
        this.classroomAdmin = classroomAdmin;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassroomCreationDate() {
        return classroomCreationDate;
    }

    public void setClassroomCreationDate(String classroomCreationDate) {
        this.classroomCreationDate = classroomCreationDate;
    }

    public String getClassroomCreationTime() {
        return classroomCreationTime;
    }

    public void setClassroomCreationTime(String classroomCreationTime) {
        this.classroomCreationTime = classroomCreationTime;
    }

    public String getClassroomAdmin() {
        return classroomAdmin;
    }

    public void setClassroomAdmin(String classroomAdmin) {
        this.classroomAdmin = classroomAdmin;
    }
}
