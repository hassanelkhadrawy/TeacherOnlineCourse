package com.example.teacheronlinecourse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {

    private Comfortaa_Regular registerName;
    private Comfortaa_Regular registerEmail;
    private Comfortaa_Regular registerPassword;
    private DatabaseReference databaseReference;
    private ConstraintLayout registerCountainer;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        Action();
    }

    private void initView() {
        registerName = (Comfortaa_Regular) findViewById(R.id.registerName);
        registerEmail = (Comfortaa_Regular) findViewById(R.id.registerEmail);
        registerPassword = (Comfortaa_Regular) findViewById(R.id.registerPassword);
        registerCountainer = (ConstraintLayout) findViewById(R.id.registerCountainer);
        back = (ImageView) findViewById(R.id.back);
    }

    private void Action(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });
    }

    public void Register(View view) {
        if (!Commans.isConnectToInternet(this)) {

            Snackbar.make(registerCountainer, getString(R.string.check_internet), Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(registerName.getText().toString())){
            registerName.setError(getString(R.string.enter_name));
            registerName.requestFocus();

        }else if (TextUtils.isEmpty(registerEmail.getText().toString())){
            registerEmail.setError(getString(R.string.enter_email));
            registerEmail.requestFocus();

        }else if (TextUtils.isEmpty(registerPassword.getText().toString())){
            registerPassword.setError(getString(R.string.enter_password));
            registerPassword.requestFocus();


        }else {
            Commans.Prograss(Registration.this, getString(R.string.waiting));
            Commans.progressDialog.show();
            String Name = registerName.getText().toString();
            String Email = registerEmail.getText().toString();
            String Password = registerPassword.getText().toString();

            SendTeacherData(Name, Email, Password);

        }
    }

    private void SendTeacherData(String Name, final String Email, String Password) {

        final RegisterModel registerModel = new RegisterModel(Name, Email, Password);
        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child(Email.replace(".","Dot")).exists()){

                    databaseReference.child(Email.replace(".", "Dot")).setValue(registerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Commans.progressDialog.dismiss();
                            Snackbar.make(registerCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                            Commans.registerModel = registerModel;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Commans.progressDialog.dismiss();
                            Snackbar.make(registerCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(registerCountainer, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Registration.this,Login.class));
        finish();
    }
}
