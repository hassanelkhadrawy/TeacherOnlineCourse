package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.ChapterAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.ChaptersModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.CoursesEnroledModel;
import com.example.teacheronlinecourse.Models.FAvouriteModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.Models.SearchModel;
import com.example.teacheronlinecourse.Models.TopCourseModel;
import com.example.teacheronlinecourse.Models.UserCoursesModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class CourseInformation extends AppCompatActivity {

    private ImageView courseImage;
    private TextView coursechapternum;
    private TextView courseDescription;
    private TextView enrol;
    private TextView addRate;
    private TextView Like;
    private TextView EditCourse;
    private TextView exams;
    private String courseID, categoryName, courseName, courseImageUrl;
    private FirebaseRecyclerAdapter<ChaptersModel, ChapterAdapter> recyclerAdapter;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ImageButton addCourseImage;
    private Comfortaa_Regular addCourseName;
    private Comfortaa_Regular addCourseDes;
    private Comfortaa_Bold cancleAddCourse;
    private Comfortaa_Bold uploadCourse;
    private Uri ImageUri;
    private RatingBar ratingBar2;
    private RecyclerView recycler;
    private Comfortaa_Regular addChapterName;
    private Comfortaa_Bold cancleAddChapter;
    private Comfortaa_Bold uploadChapter;
    private LinearLayout courseCountainer;
    private int CourseCount;
    private int user_enroled_counter;
    private DatabaseReference dREnroled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_information);
        savedInstanceState = getIntent().getExtras();
        courseID = savedInstanceState.getString("courseID");
        categoryName = savedInstanceState.getString("categoryName");

       dREnroled = FirebaseDatabase.getInstance().getReference("CoursesEnroled").child(courseID);
        dREnroled.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    CoursesEnroledModel coursesEnroledModel=dataSnapshot.getValue(CoursesEnroledModel.class);

                    user_enroled_counter=coursesEnroledModel.getCounter_enroled_user()+1;

                }else {
                    user_enroled_counter=1;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initView();
        HideAdminItems();
        GetCourseInformation();
        AddCourseCounter();
        Action();


    }

    private void initView() {
        courseImage = (ImageView) findViewById(R.id.courseImage);
        coursechapternum = findViewById(R.id.course_num_chapter);
        courseDescription = findViewById(R.id.descrition);
        enrol = findViewById(R.id.enrol);
        addRate = findViewById(R.id.addRate);
        EditCourse = findViewById(R.id.editcourse);
        Like=findViewById(R.id.like);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        courseCountainer = (LinearLayout) findViewById(R.id.courseCountainer);
        exams = (TextView) findViewById(R.id.exams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        Commans.hidenAdd(menu);
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

                    courseName = courseModel.getCourse_name();
                    courseImageUrl = courseModel.getCourse_image();
                    courseDescription.setText(courseModel.getCourse_descrition());
                    DatabaseReference databaseReferenceuser = FirebaseDatabase.getInstance().getReference("UserCourses");
                    databaseReferenceuser.child(Commans.registerModel.getEmail().replace(".", "Dot")).child("Enroled").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    UserCoursesModel userCoursesModel = userSnapshot.getValue(UserCoursesModel.class);
                                    if (userCoursesModel.getCourse_id().equals(courseModel.getCourse_id())) {
                                        enrol.setVisibility(View.GONE);
                                        Like.setVisibility(View.VISIBLE);
                                        addRate.setVisibility(View.VISIBLE);
                                        recycler.setVisibility(View.VISIBLE);
                                        exams.setVisibility(View.VISIBLE);
                                        GetChapters();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    databaseReference = FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".", "Dot")).child(courseID);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                FAvouriteModel fAvouriteModel = dataSnapshot.getValue(FAvouriteModel.class);
                                if (fAvouriteModel.isFavourite()) {
                                    Like.setText("unlike");
                                } else {
                                    Like.setText("Like");


                                }

                            }else {

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
                databaseReference = FirebaseDatabase.getInstance().getReference("UserCourses");
                UserCoursesModel userCoursesModel = new UserCoursesModel(categoryName, courseID, courseName, courseImageUrl);
                databaseReference.child(Commans.registerModel.getEmail().replace(".", "Dot")).child("Enroled").child(String.valueOf(System.currentTimeMillis())).setValue(userCoursesModel);

                CoursesEnroledModel coursesEnroledModel=new CoursesEnroledModel(user_enroled_counter);
                dREnroled.setValue(coursesEnroledModel);


                enrol.setVisibility(View.GONE);
                Like.setVisibility(View.VISIBLE);
                addRate.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.VISIBLE);
                exams.setVisibility(View.VISIBLE);

                databaseReference = FirebaseDatabase.getInstance().getReference("TopCourses").child(categoryName).child(courseID);
                TopCourseModel topCourseModel = new TopCourseModel(courseID, CourseCount + 1);
                databaseReference.setValue(topCourseModel);
                GetChapters();
            }
        });


        addRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Create_Rate();
            }
        });

        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavourite;


                isFavourite = readStateFavourite();
                if (isFavourite) {
                    Like.setText("unlike");
                    isFavourite = false;
                    AddFavouriteِ();
                    saveStateFavourite(isFavourite);


                } else {
                    Like.setText("Like");
                    isFavourite = true;
                    DeleteFavouriteِ();
                    saveStateFavourite(isFavourite);


                }
            }
        });

        EditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCourseDialog();
            }
        });

        exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseInformation.this, Exams.class);
                intent.putExtra("courseID", courseID);
                intent.putExtra("categoryName", categoryName);
                startActivity(intent);
            }
        });

    }

    private void AddCourseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_course_item, null);
        addCourseImage = (ImageButton) view.findViewById(R.id.add_course_image);
        addCourseName = (Comfortaa_Regular) view.findViewById(R.id.addCourseName);
        addCourseDes = (Comfortaa_Regular) view.findViewById(R.id.addCoursDescribtion);
        cancleAddCourse = (Comfortaa_Bold) view.findViewById(R.id.cancleAddCourse);
        uploadCourse = (Comfortaa_Bold) view.findViewById(R.id.uploadCourse);


        Picasso.with(this).load(courseImageUrl).placeholder(R.drawable.ic_perm_identity_black_24dp).into(addCourseImage);
        addCourseName.setText(courseName);
        addCourseDes.setText(courseDescription.getText().toString());
        Commans.Prograss(this, getString(R.string.waiting));


        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();


        addCourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commans.SelectImage(1, CourseInformation.this);
            }
        });
        uploadCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(addCourseName.getText().toString())) {
                    Snackbar.make(courseCountainer, getString(R.string.enter_name), Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(addCourseDes.getText().toString())) {
                    Snackbar.make(courseCountainer, getString(R.string.enter_description), Snackbar.LENGTH_SHORT).show();

                } else {
                    Commans.progressDialog.show();
                    UploadCourseData(dialog);
                }
            }
        });
        cancleAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ImageUri = data.getData();
                addCourseImage.setImageURI(ImageUri);
            }

        } else {
            Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show();


        }
    }

    private void UploadCourseData(final AlertDialog dialog) {
        String imgName = UUID.randomUUID().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(categoryName);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + imgName);

        if (ImageUri != null) {
            storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            CourseModel courseModel = new CourseModel(uri.toString(), addCourseName.getText().toString(), addCourseDes.getText().toString(), courseID);
                            databaseReference.child(courseID).setValue(courseModel);

                            // add to search

                            databaseReference = FirebaseDatabase.getInstance().getReference("Search");
                            SearchModel searchModel = new SearchModel(categoryName, courseID, addCourseName.getText().toString());
                            databaseReference.child(courseID).setValue(searchModel);

                            ImageUri = null;
                            dialog.dismiss();
                            Commans.progressDialog.dismiss();
                            Snackbar.make(courseCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Commans.progressDialog.dismiss();
                    Snackbar.make(courseCountainer, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                }
            });
        } else {
            CourseModel courseModel = new CourseModel(courseImageUrl, addCourseName.getText().toString(), addCourseDes.getText().toString(), courseID);
            databaseReference.child(courseID).setValue(courseModel);
            //  add to search

            databaseReference = FirebaseDatabase.getInstance().getReference("Search");
            SearchModel searchModel = new SearchModel(categoryName, courseID, addCourseName.getText().toString());
            databaseReference.child(courseID).setValue(searchModel);

            dialog.dismiss();
            Commans.progressDialog.dismiss();
            Snackbar.make(courseCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

        }

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

    private void AddCourseCounter() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TopCourses").child(categoryName).child(courseID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TopCourseModel topCourseModel = dataSnapshot.getValue(TopCourseModel.class);
                    CourseCount = topCourseModel.getCours_count();

                } else {
                    CourseCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetChapters() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CoursesData").child(categoryName).child(courseID).child("Chapters");
        recyclerAdapter = new FirebaseRecyclerAdapter<ChaptersModel, ChapterAdapter>(ChaptersModel.class, R.layout.chapter_item, ChapterAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(final ChapterAdapter chapterAdapter, ChaptersModel chaptersModel, final int i) {
                chapterAdapter.ChapterName.setText(chaptersModel.getChapter_name());
                chapterAdapter.ChapterNum.setText((i + 1) + "");
                coursechapternum.setText((i + 1) + " Chapters");


                int position = readState();
                if (position >= i + 1) {
                    chapterAdapter.ChapterNum.setBackgroundResource(R.drawable.background_fillcircle);
                    chapterAdapter.ChapterNum.setTextColor(Color.parseColor("#FFFFFF"));
                }

                chapterAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = readState();
                        if (position <= i + 1) {
                            chapterAdapter.ChapterNum.setBackgroundResource(R.drawable.background_fillcircle);
                            chapterAdapter.ChapterNum.setTextColor(Color.parseColor("#FFFFFF"));
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

    private void saveStateFavourite(boolean isFavourite) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.commit();
    }

    private boolean readStateFavourite() {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);
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

    private void HideAdminItems() {
        for (int i = 0; i < Commans.adminList.size(); i++) {
            if (!Commans.registerModel.getEmail().equals(Commans.adminList.get(i))) {
                EditCourse.setVisibility(View.GONE);
            }else {
                EditCourse.setVisibility(View.VISIBLE);
                break;

            }
        }

    }
    private void AddFavouriteِ() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".", "Dot")).child(courseID);
        FAvouriteModel fAvouriteModel = new FAvouriteModel(true, courseImageUrl, courseName, categoryName);
        databaseReference.setValue(fAvouriteModel);

    }

    private void DeleteFavouriteِ() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".", "Dot")).child(courseID);
        databaseReference.removeValue();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
