package com.example.teacheronlinecourse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.Models.UserCoursesModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserCourses extends AppCompatActivity {

    private RecyclerView usercourserecycler;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<UserCoursesModel,CoursesAdapter>firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_courses);
        initView();

        retriveCourses();
    }

    private void initView() {
        usercourserecycler = (RecyclerView) findViewById(R.id.usercourserecycler);
    }

    private void retriveCourses() {

        databaseReference = FirebaseDatabase.getInstance().getReference("UserCourses").child(Commans.registerModel.getEmail().replace(".","Dot")).child("Enroled");
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserCoursesModel, CoursesAdapter>(UserCoursesModel.class,R.layout.courses_item,CoursesAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final CoursesAdapter coursesAdapter, final UserCoursesModel userCoursesModel, int i) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(userCoursesModel.getCategory_name());

                databaseReference.child(userCoursesModel.getCourse_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()){

                            if (!userCoursesModel.getCourse_image().equals("null")) {

                                Picasso.with(UserCourses.this).load(userCoursesModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
                            } else {
                                coursesAdapter.courseImage.setVisibility(View.GONE);

                            }
                            coursesAdapter.CourseName.setText(userCoursesModel.getCourse_name());

                            DatabaseReference  databaseReference3 = FirebaseDatabase.getInstance().getReference("CoursesRates");
                            databaseReference3.child(userCoursesModel.getCategory_name()).child(userCoursesModel.getCourse_id()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    RateModel rateModel;
                                    float rate = 0;

                                    for (DataSnapshot rateSnapshot : dataSnapshot.getChildren()) {
                                        rateModel = rateSnapshot.getValue(RateModel.class);
                                        rate+=rateModel.getRate();
                                    }

                                    rate=rate/dataSnapshot.getChildrenCount();
                                    coursesAdapter.ratingBar.setRating(rate);



                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Commans .FavouriteFunction(databaseReference,coursesAdapter,userCoursesModel.getCourse_id());

                            coursesAdapter.courseImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(UserCourses.this, CourseInformation.class);
                                    intent.putExtra("categoryName", userCoursesModel.getCategory_name());
                                    intent.putExtra("courseID", userCoursesModel.getCourse_id());
                                    startActivity(intent);

                                }
                            });

                        }else {
                            ViewGroup.LayoutParams params = coursesAdapter.itemView.getLayoutParams();
                            params.height = 0;
                            coursesAdapter.itemView.setLayoutParams(params);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }


        };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        usercourserecycler.setLayoutManager(new LinearLayoutManager(UserCourses.this, LinearLayoutManager.VERTICAL,false));
        usercourserecycler.setAdapter(firebaseRecyclerAdapter);




    }
}
