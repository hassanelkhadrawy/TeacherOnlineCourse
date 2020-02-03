package com.example.teacheronlinecourse.Models;

public class UserCourses {
    String category_name;
    String course_id;

    public UserCourses(String category_name, String course_id) {
        this.category_name = category_name;
        this.course_id = course_id;
    }

    public UserCourses() {
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
