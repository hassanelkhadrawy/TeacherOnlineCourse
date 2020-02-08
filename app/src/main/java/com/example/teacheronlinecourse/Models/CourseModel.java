package com.example.teacheronlinecourse.Models;

public class CourseModel {
    String course_image;
    String course_name;
    String course_descrition;
    String course_id;

    public CourseModel(String course_image, String course_name, String course_descrition, String course_id) {
        this.course_image = course_image;
        this.course_name = course_name;
        this.course_descrition = course_descrition;
        this.course_id = course_id;
    }

    public CourseModel() {
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_descrition() {
        return course_descrition;
    }

    public void setCourse_descrition(String course_descrition) {
        this.course_descrition = course_descrition;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
