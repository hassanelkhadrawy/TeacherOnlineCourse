package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.ChapterAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.ChaptersModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.Models.UserCourses;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class CourseInformation extends AppCompatActivity {

    private ImageView courseImage;
    private TextView courseNumchapter;
    private TextView courseDescription;
    private TextView enrol;
    private String courseID, courseImageUrl, categoryName;
    private FirebaseRecyclerAdapter<ChaptersModel, ChapterAdapter> recyclerAdapter;
    private String usercourseid;
    private DatabaseReference databaseReference;
    private TextView addRate;
    private RatingBar ratingBar2;
    private RecyclerView recycler;
    private Comfortaa_Regular addChapterName;
    private Comfortaa_Bold cancleAddChapter;
    private Comfortaa_Bold uploadChapter;
    private LinearLayout courseCountainer;
    private TextView exams;

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
        courseNumchapter = findViewById(R.id.course_num_chapter);
        courseDescription = findViewById(R.id.descrition);
        enrol = findViewById(R.id.enrol);
        addRate = findViewById(R.id.addRate);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        courseCountainer = (LinearLayout) findViewById(R.id.courseCountainer);
        exams = (TextView) findViewById(R.id.exams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item) {

            ChapterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ChapterDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_chapter_item, null);
        addChapterName = (Comfortaa_Regular) view.findViewById(R.id.addChapterName);
        cancleAddChapter = (Comfortaa_Bold) view.findViewById(R.id.cancleAddChapter);
        uploadChapter = (Comfortaa_Bold) view.findViewById(R.id.uploadChapter);

        Commans.Prograss(this, getString(R.string.waiting));


        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        uploadChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendChapterData(dialog);
            }
        });
        cancleAddChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);

    }

    private void SendChapterData(AlertDialog dialog) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("CoursesData");
        final String chapterID = UUID.randomUUID().toString();
        ChaptersModel chaptersModel = new ChaptersModel(addChapterName.getText().toString());
        databaseReference.child(categoryName).child(courseID).child("Chapters").child(String.valueOf(System.currentTimeMillis())).setValue(chaptersModel);
        dialog.dismiss();
        Snackbar.make(courseCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();


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
                    courseDescription.setText(courseModel.getCourse_descrition());
                    DatabaseReference databaseReferenceuser = FirebaseDatabase.getInstance().getReference("UserCourses");
                    databaseReferenceuser.child(Commans.registerModel.getEmail().replace(".", "Dot")).child("Enroled").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    UserCourses userCourses = userSnapshot.getValue(UserCourses.class);
                                    if (userCourses.getCourse_id().equals(courseModel.getCourse_id())) {
                                        enrol.setVisibility(View.GONE);
                                        recycler.setVisibility(View.VISIBLE);
                                        GetChapters();
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
                recycler.setVisibility(View.VISIBLE);
                GetChapters();
            }
        });


        addRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Create_Rate();
            }
        });

        exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseInformation.this,Exams.class);
                intent.putExtra("courseID",courseID);
                intent.putExtra("categoryName",categoryName);
                startActivity(intent);
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


                        RateModel rateModel = new RateModel(ratingBar2.getRating());
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

    private void GetChapters() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CoursesData").child(categoryName).child(courseID).child("Chapters");
        recyclerAdapter = new FirebaseRecyclerAdapter<ChaptersModel, ChapterAdapter>(ChaptersModel.class, R.layout.chapter_item, ChapterAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(final ChapterAdapter chapterAdapter, ChaptersModel chaptersModel, final int i) {
                chapterAdapter.ChapterName.setText(chaptersModel.getChapter_name());
                chapterAdapter.ChapterNum.setText("" + (i + 1));
                courseNumchapter.setText((i + 1) + " Chapter");


                int position = readState();
                if (position >= i + 1) {
                    chapterAdapter.ChapterNum.setBackgroundResource(R.drawable.background_fillcircle);
                }

                chapterAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = readState();
                        if (position <= i + 1) {
                            chapterAdapter.ChapterNum.setBackgroundResource(R.drawable.background_fillcircle);
                            saveState(i + 1);

                        }
                        Intent intent = new Intent(CourseInformation.this, Files.class);
                        intent.putExtra("courseID", courseID);
                        intent.putExtra("categoryName", categoryName);
                        intent.putExtra("chapterID", recyclerAdapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerAdapter.notifyDataSetChanged();
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(recyclerAdapter);

    }

    private void saveState(int position) {
        SharedPreferences aSharedPreferences = getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putInt(courseID, position);
        aSharedPreferencesEdit.commit();
    }

    private int readState() {
        SharedPreferences aSharedPreferences = getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getInt(courseID, 0);
    }
}
