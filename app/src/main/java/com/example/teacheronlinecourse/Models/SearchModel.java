package com.example.teacheronlinecourse.Models;

public class SearchModel {
    String category_name;
    String coueseID;
    String course_name;

    public SearchModel(String category_name, String coueseID, String course_name) {
        this.category_name = category_name;
        this.coueseID = coueseID;
        this.course_name = course_name;
    }

    public SearchModel() {
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCoueseID() {
        return coueseID;
    }

    public void setCoueseID(String coueseID) {
        this.coueseID = coueseID;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
