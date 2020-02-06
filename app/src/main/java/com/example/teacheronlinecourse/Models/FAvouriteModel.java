package com.example.teacheronlinecourse.Models;

public class FAvouriteModel {
    boolean favourite;
    String course_image;
    String course_name;
    String course_category;


    public FAvouriteModel(boolean favourite, String course_image, String course_name, String course_category) {
        this.favourite = favourite;
        this.course_image = course_image;
        this.course_name = course_name;
        this.course_category = course_category;
    }

    public String getCourse_category() {
        return course_category;
    }

    public void setCourse_category(String course_category) {
        this.course_category = course_category;
    }

    public FAvouriteModel() {
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

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
