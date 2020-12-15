package com.akshat.dsmnruandroidapp.admin.students;

public class StudentsData {
    private String name, email, rollno, year, category, image, password, key;

    public StudentsData() {
    }

    public StudentsData(String name, String email, String rollno, String year, String category, String image, String password, String key) {
        this.name = name;
        this.email = email;
        this.rollno = rollno;
        this.year = year;
        this.category = category;
        this.image = image;
        this.password = password;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getYear() { return year; }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
