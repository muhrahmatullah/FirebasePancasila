package com.rahmat.app.firebasepancasila.model;

public class Student {
    private String name, major, university;

    public Student() {
    }

    public Student(String name, String major, String university) {
        this.name = name;
        this.major = major;
        this.university = university;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
