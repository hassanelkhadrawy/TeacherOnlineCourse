package com.example.teacheronlinecourse.Models;

public class CoursesEnroledModel {
     int Counter_enroled_user;

    public CoursesEnroledModel(int counter_enroled_user) {
        Counter_enroled_user = counter_enroled_user;
    }

    public CoursesEnroledModel() {
    }

    public int getCounter_enroled_user() {
        return Counter_enroled_user;
    }

    public void setCounter_enroled_user(int counter_enroled_user) {
        Counter_enroled_user = counter_enroled_user;
    }
}
