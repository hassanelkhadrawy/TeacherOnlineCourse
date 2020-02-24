package com.example.teacheronlinecourse.Activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class UserCoursesTAb extends Fragment {
    private RecyclerView usercourserecycler;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<UserCoursesModel, CoursesAdapter> firebaseRecyclerAdapter;


    public UserCoursesTAb() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_courses_tab, container, false);
        initView(view);
        retriveCourses();
        return view;
    }
    private void initView(View view) {
        usercourserecycler = (RecyclerView) view.findViewById(R.id.usercourserecycler);
    }

    private void retriveCourses() {

        databaseReference = FirebaseDatabase.getInstance().getReference("UserCourses").child(Commans.registerModel.getEmail().replace(".","Dot")).child("Enroled");
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserCoursesModel, CoursesAdapter>(UserCoursesModel.class,R.layout.courses_item,CoursesAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final CoursesAdapter coursesAdapter, final UserCoursesModel userCoursesModel, int i) {

                if (!userCoursesModel.getCourse_image().equals("null")) {

                    Picasso.with(getActivity()).load(userCoursesModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
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

                        Intent intent = new Intent(getActivity(), CourseInformation.class);
                        intent.putExtra("categoryName", userCoursesModel.getCategory_name());
                        intent.putExtra("courseID", userCoursesModel.getCourse_id());
                        startActivity(intent);

                    }
                });



            }


        };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        usercourserecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        usercourserecycler.setAdapter(firebaseRecyclerAdapter);




    }
}
