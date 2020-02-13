package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.AdminModel;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.Models.VerificationModel;
import com.example.teacheronlinecourse.R;
import com.example.teacheronlinecourse.Services.ApiClient;
import com.example.teacheronlinecourse.Services.JsonPlaceHolderApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Registration extends AppCompatActivity {

    private Comfortaa_Regular registerName;
    private Comfortaa_Regular registerEmail;
    private Comfortaa_Regular registerPassword;
    private DatabaseReference databaseReference;
    private LinearLayout registerCountainer;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        jsonPlaceHolderApi = ApiClient.getApiClient().create(JsonPlaceHolderApi.class);
        initView();
        Action();
    }

    private void initView() {
        registerName = (Comfortaa_Regular) findViewById(R.id.registerName);
        registerEmail = (Comfortaa_Regular) findViewById(R.id.registerEmail);
        registerPassword = (Comfortaa_Regular) findViewById(R.id.registerPassword);
        registerCountainer = (LinearLayout) findViewById(R.id.registerCountainer);
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


        }else if (!isEmailValid(registerEmail.getText().toString())){
            Toast.makeText(this, R.string.enter_valid_email, Toast.LENGTH_SHORT).show();

        }else if (registerPassword.getText().toString().length() <6){
            registerPassword.setError("Password should more than 6 charcters");
            registerPassword.requestFocus();
        }
        else {
            Commans.Prograss(Registration.this, getString(R.string.waiting));
            Commans.progressDialog.show();
            String Name = registerName.getText().toString();
            String Email = registerEmail.getText().toString();
            String Password = registerPassword.getText().toString();

            Verification(Email);

        }
    }

    private void SendUserData(String Name, final String Email, String Password) {

        Commans.progressDialog.show();
        final RegisterModel registerModel = new RegisterModel(Name, Email, Password);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child(Email.replace(".","Dot")).exists()){

                    databaseReference.child(Email.replace(".", "Dot")).setValue(registerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Commans.registerModel = registerModel;
                            Commans.progressDialog.dismiss();
                            Snackbar.make(registerCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                            saveState();
                            startActivity(new Intent(Registration.this, Home.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Commans.progressDialog.dismiss();
                            Snackbar.make(registerCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }
                    });
                }else {
                    Commans.progressDialog.dismiss();
                    Toast.makeText(Registration.this, R.string.emailexist, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(registerCountainer, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });


    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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


    private void Verification(String Email) {


        Call<VerificationModel> call = jsonPlaceHolderApi.verifi(Email);

        call.enqueue(new Callback<VerificationModel>() {
            @Override
            public void onResponse(Call<VerificationModel> call, Response<VerificationModel> response) {

                if (response.isSuccessful()){
                    VerificationModel verification_model = response.body();
                    Commans.progressDialog.dismiss();
                    VerificationALert(verification_model.getCode());



                }else {
                    Commans.progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<VerificationModel> call, Throwable t) {

            }
        });
    }


    private void VerificationALert(final int code) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText=new EditText(this);

        editText.setHint(R.string.Enter_Verfication_Code);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        builder.setView(editText);


        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button Positive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button Cancel = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (editText.getText().toString().isEmpty()){
                            editText.setError(getString(R.string.enter_code));
                            editText.requestFocus();

                        }else {
                            int numberCode = Integer.parseInt(editText.getText().toString());
                            if (numberCode==code){
                                SendUserData(registerName.getText().toString(), registerEmail.getText().toString(), registerPassword.getText().toString());

                            }else {
                                editText.setText("");
                                editText.setError(getString(R.string.wrong_code));
                                editText.requestFocus();
                            }
                        }


                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Registration.this,Login.class));
        finish();
    }
}
