package com.example.teacheronlinecourse.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.multidex.MultiDex;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private LinearLayout loginCountainer;
    private EditText loginEmail;
    private EditText loginPassword;
    private DatabaseReference databaseReference;
    private GoogleSignInOptions gso;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideKeyboard(this);
        initView();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void Login(View view) {


        if (!Commans.isConnectToInternet(this)) {

            Snackbar.make(loginCountainer, getString(R.string.check_internet), Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(loginEmail.getText().toString())) {
            loginEmail.setError(getString(R.string.enter_email));
            loginEmail.requestFocus();

        } else if (TextUtils.isEmpty(loginPassword.getText().toString())) {
            loginPassword.setError(getString(R.string.enter_password));


        } else {
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
                            finish();

                        }
                    } else {
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


    private void initView() {
        loginCountainer = (LinearLayout) findViewById(R.id.loginCountainer);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        Commans.Prograss(Login.this, getString(R.string.waiting));

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void CreatAccount(View view) {
        startActivity(new Intent(Login.this, Registration.class));
        finish();
    }



    private void saveState() {
        SharedPreferences aSharedPreferences = getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putString("Email", Commans.registerModel.getEmail());
        aSharedPreferencesEdit.putString("Password", Commans.registerModel.getPassword());
        aSharedPreferencesEdit.commit();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            GetGoogleInfo();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Snackbar.make(loginCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }
    private void GetGoogleInfo(){

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            SendUserData(personName,personEmail,personId,String.valueOf(personPhoto));

        }
    }

    private void SendUserData(String Name, final String Email, String Password,String Image) {

        final RegisterModel registerModel = new RegisterModel(Name, Email, Password,Image);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    databaseReference.child(Email.replace(".", "Dot")).setValue(registerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Commans.registerModel = registerModel;
                            Commans.progressDialog.dismiss();
                            Snackbar.make(loginCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                            saveState();
                            startActivity(new Intent(Login.this, Home.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Commans.progressDialog.dismiss();
                            Snackbar.make(loginCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(loginCountainer, "" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });


    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
