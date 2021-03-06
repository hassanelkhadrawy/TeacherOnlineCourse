package com.example.teacheronlinecourse.Activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CategoryAdapter;
import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Adapters.SliderAdapter;
import com.example.teacheronlinecourse.Adapters.TopCoursesAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.FAvouriteModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.Models.TopCourseModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTab extends Fragment {


    private RecyclerView courseRecycler;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<CategoryModel, TopCoursesAdapter> recyclerAdapter;
    private FirebaseRecyclerAdapter<CategoryModel, CategoryAdapter> categoryAdapter;
    private FirebaseRecyclerAdapter<CourseModel, CoursesAdapter> recyclercourseAdapter;
    private RecyclerView categoryRecycler;

    public HomeTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);
        hideKeyboard(getActivity());
        initView(view);
        SLider(view);
        GetCategory();
        retriveTopCourses();
        Commans.GetAdmins(databaseReference);
        return view;
    }

    private void initView(View view) {
        courseRecycler = (RecyclerView) view.findViewById(R.id.course_recycler);
        categoryRecycler = (RecyclerView) view.findViewById(R.id.category_recycler);

    }

    private void GetCategory() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        categoryAdapter = new FirebaseRecyclerAdapter<CategoryModel, CategoryAdapter>(CategoryModel.class, R.layout.category_item, CategoryAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(CategoryAdapter categoryAdapter, final CategoryModel categoryModel, int i) {


                categoryAdapter.Category.setText(categoryModel.getName());
                categoryAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Courses.class);
                        intent.putExtra("CategoryName", categoryModel.getName());
                        startActivity(intent);
                    }
                });
            }
        };


        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setAdapter(categoryAdapter);


    }

    private void retriveTopCourses() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        recyclerAdapter = new FirebaseRecyclerAdapter<CategoryModel, TopCoursesAdapter>(CategoryModel.class, R.layout.top_course_item, TopCoursesAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(final TopCoursesAdapter topcoursesAdapter, final CategoryModel categoryModel, final int i) {

                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Courses").child(categoryModel.getName());
                final ViewGroup.LayoutParams topcourseparams = topcoursesAdapter.itemView.getLayoutParams();


                recyclercourseAdapter = new FirebaseRecyclerAdapter<CourseModel, CoursesAdapter>(CourseModel.class, R.layout.course_item, CoursesAdapter.class, databaseReference2) {
                    @Override
                    protected void populateViewHolder(final CoursesAdapter coursesAdapter, final CourseModel courseModel, int position) {
                        final ViewGroup.LayoutParams courseparams = coursesAdapter.itemView.getLayoutParams();
//
//                        topcourseparams.height = 0;
//                        topcoursesAdapter.itemView.setLayoutParams(topcourseparams);

                        if (position < 5) {


                            if (!courseModel.getCourse_image().equals("null")) {

                                Picasso.with(getActivity()).load(courseModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
                            } else {
                                coursesAdapter.courseImage.setVisibility(View.GONE);

                            }
                            coursesAdapter.CourseName.setText(courseModel.getCourse_name());


                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("CoursesRates");
                            databaseReference3.child(categoryModel.getName()).child(courseModel.getCourse_id()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    RateModel rateModel;
                                    float rate = 0;

                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot rateSnapshot : dataSnapshot.getChildren()) {
                                            rateModel = rateSnapshot.getValue(RateModel.class);
                                            rate += rateModel.getRate();
                                        }

                                        rate = rate / dataSnapshot.getChildrenCount();
                                        coursesAdapter.ratingBar.setRating(rate);

                                        if (rate >= 3.5) {

                                            courseparams.height = courseparams.WRAP_CONTENT;
                                            coursesAdapter.itemView.setLayoutParams(courseparams);
                                            topcourseparams.height = topcourseparams.WRAP_CONTENT;
                                            topcoursesAdapter.itemView.setLayoutParams(topcourseparams);
                                            topcoursesAdapter.TopText.setText("Top courses in " + categoryModel.getName());


                                        } else {
                                            courseparams.height = 0;
                                            courseparams.width=0;
                                            coursesAdapter.itemView.setLayoutParams(courseparams);

                                        }
                                    } else {
                                        courseparams.height = 0;
                                        courseparams.width=0;
                                        coursesAdapter.itemView.setLayoutParams(courseparams);

                                    }
                                }



                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

//
                            if (TextUtils.isEmpty(topcoursesAdapter.TopText.getText().toString())){
                                topcourseparams.height = 0;
//                                topcourseparams.width = 0;
                                topcoursesAdapter.itemView.setLayoutParams(topcourseparams);

                            }
//                            else {
//                                topcourseparams.height = 0;
//                                topcoursesAdapter.itemView.setLayoutParams(topcourseparams);
//
//                            }

                            Commans.FavouriteFunction(databaseReference, coursesAdapter, courseModel.getCourse_id());


                            coursesAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(getActivity(), CourseInformation.class);
                                    intent.putExtra("categoryName", categoryModel.getName());
                                    intent.putExtra("courseID", courseModel.getCourse_id());
                                    startActivity(intent);

                                }
                            });
                        }

                    }
                };


                topcoursesAdapter.topRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                topcoursesAdapter.topRecycler.setAdapter(recyclercourseAdapter);


            }
        };
        recyclerAdapter.notifyDataSetChanged();
        courseRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        courseRecycler.setAdapter(recyclerAdapter);



    }


    private void SLider(View view) {
        SliderView sliderView = view.findViewById(R.id.imageSlider);

        SliderAdapter adapter = new SliderAdapter(getActivity());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();

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


}
