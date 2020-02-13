package com.example.teacheronlinecourse.Models;

public class UserCoursesModel {
    String category_name;
    String course_id;
    String course_name;
    String course_image;


    public UserCoursesModel(String category_name, String course_id, String course_name, String course_image) {
        this.category_name = category_name;
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_image = course_image;
    }

    public UserCoursesModel() {
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

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }
}
