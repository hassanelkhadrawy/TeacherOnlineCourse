package com.example.teacheronlinecourse.Models;

public class AdminModel {
    String admin_email;

    public AdminModel(String admin_email) {
        this.admin_email = admin_email;
    }

    public AdminModel() {
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }
}
