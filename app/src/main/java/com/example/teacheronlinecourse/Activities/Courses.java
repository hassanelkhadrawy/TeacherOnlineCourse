package com.example.teacheronlinecourse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Courses extends AppCompatActivity {
    private FirebaseRecyclerAdapter<CourseModel, CoursesAdapter> recyclerAdapter;
    private DatabaseReference databaseReference;
    String CategoryName;
    private RecyclerView coorsesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        savedInstanceState = getIntent().getExtras();
        CategoryName = savedInstanceState.getString("CategoryName");
        initView();
        retriveCourses();
    }

    private void retriveCourses() {
        Commans.Prograss(this, getString(R.string.waiting));
        Commans.progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(CategoryName);

        recyclerAdapter = new FirebaseRecyclerAdapter<CourseModel, CoursesAdapter>(CourseModel.class, R.layout.course_item, CoursesAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(final CoursesAdapter coursesAdapter, final CourseModel courseModel, final int i) {

                if (!courseModel.getCourse_image().equals("null")) {
//                    coursesAdapter.courseImage.setVisibility(View.VISIBLE);
                    Picasso.with(Courses.this).load(courseModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
                } else {
                    coursesAdapter.courseImage.setVisibility(View.GONE);

                }

                coursesAdapter.CourseName.setText(courseModel.getCourse_name());

              DatabaseReference  databaseReference2 = FirebaseDatabase.getInstance().getReference("CoursesRates");
                databaseReference2.child(CategoryName).child(recyclerAdapter.getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
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



//                databaseReference = FirebaseDatabase.getInstance().getReference("Chapters").child(Commans.registerModel.getEmail().replace(".", "Dot"));
//                databaseReference.child(courseModel.getCourse_id()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//                            coursesAdapter.CourseChaptersNum.setText("" + dataSnapshot.getChildrenCount());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

                Commans.progressDialog.dismiss();



                coursesAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Courses.this, CourseInformation.class);
                        intent.putExtra("categoryName", CategoryName);
                        intent.putExtra("courseID", recyclerAdapter.getRef(i).getKey());
                        intent.putExtra("courseImage", courseModel.getCourse_image());
                        startActivity(intent);

                    }
                });


            }
        };
        recyclerAdapter.notifyDataSetChanged();
        coorsesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        coorsesRecycler.setAdapter(recyclerAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                recyclerAdapter.getRef(position).removeValue();

            }
        });
        helper.attachToRecyclerView(coorsesRecycler);


    }

    private void initView() {
        coorsesRecycler = (RecyclerView) findViewById(R.id.coorses_recycler);
    }
}
