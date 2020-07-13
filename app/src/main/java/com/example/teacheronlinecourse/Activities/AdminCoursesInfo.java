package com.example.teacheronlinecourse.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.CoursesEnroledModel;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.Models.UserCoursesModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminCoursesInfo extends AppCompatActivity {

    private TextView coursesNumber;
    private RecyclerView adminCoursesRecycler;
    private String category_name;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<CourseModel, FileAdapter> CourseAdapter;
    private int Counter=0;
    private int FCounter=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_courses_info);
        savedInstanceState = getIntent().getExtras();
        category_name = savedInstanceState.getString("category");
        initView();
        GetCoursesInfo();
    }

    private void initView() {
        coursesNumber = (TextView) findViewById(R.id.courses_Number);
        adminCoursesRecycler = (RecyclerView) findViewById(R.id.admin_courses_recycler);
    }
    private void GetCoursesInfo(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(category_name);

        CourseAdapter=new FirebaseRecyclerAdapter<CourseModel, FileAdapter>(CourseModel.class,R.layout.file_item,FileAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final FileAdapter fileAdapter, final CourseModel courseModel, int i) {
                databaseReference = FirebaseDatabase.getInstance().getReference("CoursesEnroled").child(courseModel.getCourse_id());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            CoursesEnroledModel coursesEnroledModel=dataSnapshot.getValue(CoursesEnroledModel.class);
                            fileAdapter.File.setText("\n"+courseModel.getCourse_name() + "        "+ coursesEnroledModel.getCounter_enroled_user() + " User \n");


                        }else {
                            fileAdapter.File.setText("\n"+courseModel.getCourse_name() + "         No users yet \n");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                 coursesNumber.setText(CourseAdapter.getItemCount() + " Course");



            }

        };
        CourseAdapter.notifyDataSetChanged();
        adminCoursesRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adminCoursesRecycler.setAdapter(CourseAdapter);

    }
}
