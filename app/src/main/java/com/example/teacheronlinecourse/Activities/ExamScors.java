package com.example.teacheronlinecourse.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.ExamScoreModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExamScors extends AppCompatActivity {

    private RecyclerView examScoreRecycler;
    private  DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<ExamScoreModel, FileAdapter>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_scors);
        initView();
    }

    private void initView() {
        examScoreRecycler = (RecyclerView) findViewById(R.id.examScoreRecycler);

         databaseReference = FirebaseDatabase.getInstance().getReference("UserExamScors").child(Commans.registerModel.getEmail().replace(".", "Dot")).child("ExamsScors");

        adapter=new FirebaseRecyclerAdapter<ExamScoreModel, FileAdapter>(ExamScoreModel.class,R.layout.file_item,FileAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final FileAdapter fileAdapter, final ExamScoreModel examScoreModel, final int i) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(examScoreModel.getCategoryName()).child(examScoreModel.getCourseID());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            CourseModel courseModel=dataSnapshot.getValue(CourseModel.class);
                            fileAdapter.File.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attach_file_black_24dp, 0, 0, 0);
                            fileAdapter.File.setText("Quez "+(i + 1)+"\n" + "Course name: "+ courseModel.getCourse_name()+"\n"+ "Score: "+examScoreModel.getScore()+" from "+examScoreModel.getNum_ques()  );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        examScoreRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        examScoreRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
