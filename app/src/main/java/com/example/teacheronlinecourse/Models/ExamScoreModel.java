package com.example.teacheronlinecourse.Models;

public class ExamScoreModel {
    String categoryName;
    String courseID;
    String examID;
    int score;
    int num_ques;
    String state;

    public ExamScoreModel(String categoryName, String courseID, String examID, int score, int num_ques, String state) {
        this.categoryName = categoryName;
        this.courseID = courseID;
        this.examID = examID;
        this.score = score;
        this.num_ques = num_ques;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNum_ques() {
        return num_ques;
    }

    public void setNum_ques(int num_ques) {
        this.num_ques = num_ques;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
