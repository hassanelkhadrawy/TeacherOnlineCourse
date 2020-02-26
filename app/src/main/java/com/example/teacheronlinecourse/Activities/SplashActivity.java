package com.example.teacheronlinecourse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar prograsssPar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        initView();
        autoLogin();
    }

    private void autoLogin() {


        SharedPreferences aSharedPreferences = getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        final String email = aSharedPreferences.getString("Email", "null");
        final String password = aSharedPreferences.getString("Password", "null");

        if (Commans.isConnectToInternet(this)) {


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

            databaseReference.child(email.replace(".", "Dot")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final RegisterModel registerModel = dataSnapshot.getValue(RegisterModel.class);
                        if (email.equals(registerModel.getEmail()) && password.equals(registerModel.getPassword())) {

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Commans.registerModel = registerModel;
                                    Intent intent = new Intent(SplashActivity.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);


                        } else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });


        } else {

            Toast.makeText(this, R.string.check_internet, Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void initView() {
        prograsssPar = (ProgressBar) findViewById(R.id.prograsssPar);
    }
}
