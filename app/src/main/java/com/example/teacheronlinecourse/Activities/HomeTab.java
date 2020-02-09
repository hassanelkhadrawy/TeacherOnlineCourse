package com.example.teacheronlinecourse.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CategoryAdapter;
import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Adapters.TopCoursesAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.FAvouriteModel;
import com.example.teacheronlinecourse.Models.RateModel;
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
public class HomeTab extends Fragment {


    private RecyclerView courseRecycler;
    private int public_position;
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
        initView(view);
        GetCategory();
        retriveTopCourses();
        return view;
    }

    private void initView(View view) {
        courseRecycler = (RecyclerView) view.findViewById(R.id.course_recycler);
        categoryRecycler = (RecyclerView)view. findViewById(R.id.category_recycler);

    }

    private void GetCategory(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        categoryAdapter=new FirebaseRecyclerAdapter<CategoryModel, CategoryAdapter>(CategoryModel.class,R.layout.category_item,CategoryAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(CategoryAdapter categoryAdapter, final CategoryModel categoryModel, int i) {


                categoryAdapter.Category.setText(categoryModel.getName());
                categoryAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),Courses.class);
                        intent.putExtra("CategoryName",categoryModel.getName());
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

                topcoursesAdapter.TopText.setText("Top courses in "+categoryModel.getName());
              DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Courses").child(categoryModel.getName());

                recyclercourseAdapter=new FirebaseRecyclerAdapter<CourseModel, CoursesAdapter>(CourseModel.class,R.layout.course_item,CoursesAdapter.class,databaseReference2) {
                    @Override
                    protected void populateViewHolder(final CoursesAdapter coursesAdapter, CourseModel courseModel, int position) {

                        public_position=position;

                        if (!courseModel.getCourse_image().equals("null")) {

//                    coursesAdapter.courseImage.setVisibility(View.VISIBLE);
                            Picasso.with(getActivity()).load(courseModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
                        } else {
                            coursesAdapter.courseImage.setVisibility(View.GONE);

                        }
                        coursesAdapter.CourseName.setText(courseModel.getCourse_name());

//                        Toast.makeText(getActivity(), ""+recyclercourseAdapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
//                        coursesAdapter.ratingBar.setVisibility(View.GONE);

                        DatabaseReference  databaseReference3 = FirebaseDatabase.getInstance().getReference("CoursesRates");
                        databaseReference3.child(categoryModel.getName()).child(courseModel.getCourse_id()).addValueEventListener(new ValueEventListener() {
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

                        FavouriteFunction(coursesAdapter,courseModel.getCourse_id());


                    }
                };


                topcoursesAdapter.topRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                topcoursesAdapter.topRecycler.setAdapter(recyclercourseAdapter);



            }
        };
        recyclerAdapter.notifyDataSetChanged();
        courseRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        courseRecycler.setAdapter(recyclerAdapter);

//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                int position = viewHolder.getAdapterPosition();
//                recyclerAdapter.getRef(position).removeValue();
//
//            }
//        });
//        helper.attachToRecyclerView(courseRecycler);


    }

    private void FavouriteFunction(final CoursesAdapter coursesAdapter ,String CourseID){
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".", "Dot")).child(CourseID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FAvouriteModel fAvouriteModel = dataSnapshot.getValue(FAvouriteModel.class);
                    if (fAvouriteModel.isFavourite()) {
                        coursesAdapter.favourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    } else {
                        coursesAdapter.favourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);

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
