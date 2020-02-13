package com.example.teacheronlinecourse.Models;

import com.google.gson.annotations.SerializedName;

public class VerificationModel {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }


}
