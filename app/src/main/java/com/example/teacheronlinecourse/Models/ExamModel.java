package com.example.teacheronlinecourse.Models;

public class ExamModel {
   public String question;
   public String answer;

    public ExamModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public ExamModel() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
