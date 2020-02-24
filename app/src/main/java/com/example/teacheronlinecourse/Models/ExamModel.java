package com.example.teacheronlinecourse.Models;

public class ExamModel {
   public String question;
   public String answer_1;
   public String wrong_answer_2;
   public String wrong_answer_3;

    public ExamModel(String question, String answer_1, String wrong_answer_2, String wrong_answer_3) {
        this.question = question;
        this.answer_1 = answer_1;
        this.wrong_answer_2 = wrong_answer_2;
        this.wrong_answer_3 = wrong_answer_3;
    }

    public ExamModel() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer_1() {
        return answer_1;
    }

    public void setAnswer_1(String answer_1) {
        this.answer_1 = answer_1;
    }

    public String getWrong_answer_2() {
        return wrong_answer_2;
    }

    public void setWrong_answer_2(String wrong_answer_2) {
        this.wrong_answer_2 = wrong_answer_2;
    }

    public String getWrong_answer_3() {
        return wrong_answer_3;
    }

    public void setWrong_answer_3(String wrong_answer_3) {
        this.wrong_answer_3 = wrong_answer_3;
    }
}
