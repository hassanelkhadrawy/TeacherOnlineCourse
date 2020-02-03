package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Light;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.Models.UserCourses;
import com.example.teacheronlinecourse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CourseInformation extends AppCompatActivity {

    private ImageView courseImage;
    private ImageView arrowBack;
    private Comfortaa_Light courseNumVideo;
    private Comfortaa_Bold enrol;
    private Comfortaa_Bold files;
    private Comfortaa_Bold videos;
    private String courseID, courseImageUrl, categoryName;
    private String usercourseid;
    private DatabaseReference databaseReference;
    private Comfortaa_Bold addRate;
    private RatingBar ratingBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_information);
        savedInstanceState = getIntent().getExtras();
        courseID = savedInstanceState.getString("courseID");
        courseImageUrl = savedInstanceState.getString("courseImage");
        categoryName = savedInstanceState.getString("categoryName");

        initView();
        GetCourseInformation();
        Action();
    }

    private void initView() {
        courseImage = (ImageView) findViewById(R.id.courseImage);
        arrowBack = (ImageView) findViewById(R.id.arrow_Back);
        courseNumVideo = (Comfortaa_Light) findViewById(R.id.course_num_video);
        enrol = (Comfortaa_Bold) findViewById(R.id.enrol);
        files = (Comfortaa_Bold) findViewById(R.id.files);
        videos = (Comfortaa_Bold) findViewById(R.id.videos);
        addRate = (Comfortaa_Bold) findViewById(R.id.addRate);
    }

    private void GetCourseInformation() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(categoryName);
        databaseReference.child(courseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    final CourseModel courseModel = dataSnapshot.getValue(CourseModel.class);
                    Picasso.with(CourseInformation.this).load(courseModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(courseImage);

                    usercourseid = courseModel.getCourse_id();
                    Toast.makeText(CourseInformation.this, usercourseid, Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReferenceuser = FirebaseDatabase.getInstance().getReference("UserCourses");
                    databaseReferenceuser.child(Commans.registerModel.getEmail().replace(".", "Dot")).child("Enroled").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    UserCourses userCourses = userSnapshot.getValue(UserCourses.class);
                                    if (userCourses.getCourse_id().equals(courseModel.getCourse_id())) {
                                        enrol.setVisibility(View.GONE);
                                        files.setVisibility(View.VISIBLE);
                                        videos.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void Action() {

        enrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCourses");
                UserCourses userCourses = new UserCourses(categoryName, usercourseid);
                databaseReference.child(Commans.registerModel.getEmail().replace(".", "Dot")).child("Enroled").child(String.valueOf(System.currentTimeMillis())).setValue(userCourses);
                enrol.setVisibility(View.GONE);
                files.setVisibility(View.VISIBLE);
                videos.setVisibility(View.VISIBLE);
            }
        });

        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseInformation.this, ChaptersActivity.class);
                intent.putExtra("courseID", courseID);
                intent.putExtra("categoryName", categoryName);
                startActivity(intent);
            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseInformation.this, ChaptersVideo.class);
                intent.putExtra("courseID", courseID);
                intent.putExtra("categoryName", categoryName);
                startActivity(intent);
            }
        });
        addRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Create_Rate();
            }
        });


    }

    private void Create_Rate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_rate_item, null);
        ratingBar2 = (RatingBar) view.findViewById(R.id.ratingBar2);


        builder.setView(view);


        builder.setCancelable(false);
        builder.setPositiveButton("Add", null);
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


                        RateModel rateModel=new RateModel(ratingBar2.getRating());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CoursesRates");
                        databaseReference.child(categoryName).child(courseID).child(Commans.registerModel.getEmail().replace(".", "Dot")).setValue(rateModel);

                        mAlertDialog.dismiss();

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

}
