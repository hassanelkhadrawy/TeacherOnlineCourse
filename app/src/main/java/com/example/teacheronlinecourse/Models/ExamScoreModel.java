package com.example.teacheronlinecourse.Models;

public class ExamScoreModel {
    String categoryName;
    String courseID;
    String examID;
    String score;

    public ExamScoreModel(String categoryName, String courseID, String examID, String score) {
        this.categoryName = categoryName;
        this.courseID = courseID;
        this.examID = examID;
        this.score = score;
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
}
