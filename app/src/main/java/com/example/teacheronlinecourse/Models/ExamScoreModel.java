package com.example.teacheronlinecourse.Models;

public class ExamScoreModel {
    String categoryName;
    String courseID;
    String examID;
    String score;
    String state;

    public ExamScoreModel(String categoryName, String courseID, String examID, String score, String state) {
        this.categoryName = categoryName;
        this.courseID = courseID;
        this.examID = examID;
        this.score = score;
        this.state = state;
    }

    public ExamScoreModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
