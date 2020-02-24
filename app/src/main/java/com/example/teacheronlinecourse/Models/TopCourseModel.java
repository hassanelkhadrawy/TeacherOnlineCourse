package com.example.teacheronlinecourse.Models;

public class TopCourseModel {
     String courseID;
     int cours_count;

    public TopCourseModel(String courseID, int cours_count) {
        this.courseID = courseID;
        this.cours_count = cours_count;
    }

    public TopCourseModel() {
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getCours_count() {
        return cours_count;
    }

    public void setCours_count(int cours_count) {
        this.cours_count = cours_count;
    }
}
