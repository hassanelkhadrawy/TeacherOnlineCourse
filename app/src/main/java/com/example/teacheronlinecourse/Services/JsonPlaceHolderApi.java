package com.example.teacheronlinecourse.Services;

import com.example.teacheronlinecourse.Models.VerificationModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {



///////////////////////////////////////////////////////////////////////////////

    // hassan


    @FormUrlEncoded
    @POST("verification.php")
    Call<VerificationModel>verifi(@Field("user_email") String email);
}