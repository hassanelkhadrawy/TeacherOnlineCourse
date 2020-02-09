package com.example.teacheronlinecourse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private ConstraintLayout loginCountainer;
    private Comfortaa_Regular loginEmail;
    private Comfortaa_Regular loginPassword;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        autoLogin();
    }

    public void Login(View view) {


        if (!Commans.isConnectToInternet(this)) {

            Snackbar.make(loginCountainer, getString(R.string.check_internet), Snackbar.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(loginEmail.getText().toString())){
            loginEmail.setError(getString(R.string.enter_email));
            loginEmail.requestFocus();

        }else if (TextUtils.isEmpty(loginPassword.getText().toString())){
            loginPassword.setError(getString(R.string.enter_password));


        }else {
            Commans.progressDialog.show();

            final String Email = loginEmail.getText().toString();
            final String Password = loginPassword.getText().toString();

            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(Email.replace(".", "Dot")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        RegisterModel registerModel = dataSnapshot.getValue(RegisterModel.class);

                        if (Email.equals(registerModel.getEmail()) && Password.equals(registerModel.getPassword())) {


                            Commans.registerModel = registerModel;
                            Commans.progressDialog.dismiss();
                            saveState();
                            startActivity(new Intent(Login.this, Home.class));

                        }
                    }else {
                        Commans.progressDialog.dismiss();
                        Snackbar.make(loginCountainer, getString(R.string.failed), Snackbar.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Commans.progressDialog.dismiss();
                    Snackbar.make(loginCountainer, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();


                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        loginCountainer = (ConstraintLayout) findViewById(R.id.loginCountainer);
        loginEmail = (Comfortaa_Regular) findViewById(R.id.loginEmail);
        loginPassword = (Comfortaa_Regular) findViewById(R.id.loginPassword);
        Commans.Prograss(Login.this,getString(R.string.waiting));

    }

    public void CreatAccount(View view) {
        startActivity(new Intent(Login.this, Registration.class));
        finish();
    }

    private void autoLogin(){
        Commans.progressDialog.show();

        SharedPreferences aSharedPreferences = getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        final String email=aSharedPreferences.getString("Email","null");
        final String password=aSharedPreferences.getString("Password","null");
        databaseReference.child(email.replace(".", "Dot")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisterModel registerModel = dataSnapshot.getValue(RegisterModel.class);

                    if (email.equals(registerModel.getEmail()) && password.equals(registerModel.getPassword())) {


                        Commans.registerModel = registerModel;
                        Commans.progressDialog.dismiss();
                        startActivity(new Intent(Login.this, Home.class));

                    }
                }else {
                    Commans.progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Commans.progressDialog.dismiss();
                Snackbar.make(loginCountainer, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();


            }
        });

    }

    private void saveState() {
        SharedPreferences aSharedPreferences =getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putString("Email", Commans.registerModel.getEmail());
        aSharedPreferencesEdit.putString("Password", Commans.registerModel.getPassword());
        aSharedPreferencesEdit.commit();
    }
}
