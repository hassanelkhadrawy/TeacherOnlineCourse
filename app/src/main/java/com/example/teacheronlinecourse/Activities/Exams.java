package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.AddExamAdapter;
import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.ExamModel;
import com.example.teacheronlinecourse.Models.ExamScoreModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Exams extends AppCompatActivity {

    private RecyclerView recyclerAadExam;
    private EditText questiontxt;
    private EditText answertxt_1;
    private EditText answertxt_2;
    private EditText answertxt_3;

    private TextView AddQuestion;
    private ArrayList<ExamModel> Questiolist = new ArrayList<>();
    private String CourseID, CategoryName;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerexam;
    FirebaseRecyclerAdapter<ExamModel, FileAdapter>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        savedInstanceState = getIntent().getExtras();
        CategoryName = savedInstanceState.getString("categoryName");
        CourseID = savedInstanceState.getString("courseID");

        initView();
        GetExams();
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

            AddExam();
        }

        return super.onOptionsItemSelected(item);
    }

    private void AddExam() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_exam_item, null);

        recyclerAadExam = (RecyclerView) view.findViewById(R.id.recycler_aadExam);
        questiontxt = (EditText) view.findViewById(R.id.questiontxt);
        answertxt_1 = (EditText) view.findViewById(R.id.answertxt1);
        answertxt_2 = (EditText) view.findViewById(R.id.answertxt2);
        answertxt_3 = (EditText) view.findViewById(R.id.answertxt3);

        AddQuestion = (TextView) view.findViewById(R.id.addquestion);
        builder.setView(view);


        AddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(questiontxt.getText().toString())) {
                    Toast.makeText(Exams.this, "Add Question", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(answertxt_1.getText().toString())) {

                    Toast.makeText(Exams.this, "Add Answer 1", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(answertxt_2.getText().toString())) {

                    Toast.makeText(Exams.this, "Add Answer 2", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(answertxt_3.getText().toString())) {

                    Toast.makeText(Exams.this, "Add Answer 3", Toast.LENGTH_SHORT).show();

                } else {
                    Questiolist.add(new ExamModel(questiontxt.getText().toString(), answertxt_1.getText().toString(),answertxt_2.getText().toString(),answertxt_3.getText().toString()));
                    AddExamAdapter addExamAdapter = new AddExamAdapter(Exams.this, Questiolist);
                    recyclerAadExam.setLayoutManager(new LinearLayoutManager(Exams.this, LinearLayoutManager.VERTICAL, false));
                    recyclerAadExam.setAdapter(addExamAdapter);
                    questiontxt.setText("");
                    answertxt_1.setText("");
                    answertxt_2.setText("");
                    answertxt_3.setText("");



                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("Upload", null);
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


                        UploadQuez(mAlertDialog);

                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Questiolist.clear();
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);


    }

    private void UploadQuez(AlertDialog mAlertDialog) {
        Commans.Prograss(this, getString(R.string.waiting));
        Commans.progressDialog.show();

        String ExamID = String.valueOf(System.currentTimeMillis());
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData");
        for (int i = 0; i < Questiolist.size(); i++) {
            ExamModel examModel = new ExamModel(Questiolist.get(i).getQuestion(), Questiolist.get(i).getAnswer_1(),Questiolist.get(i).getWrong_answer_2(),Questiolist.get(i).getWrong_answer_3());
            databaseReference.child(CategoryName).child(CourseID).child("Exams").child(ExamID).child(String.valueOf(System.currentTimeMillis())).setValue(examModel);
            Log.d("databaseReference", databaseReference.toString());

            if (i == Questiolist.size() - 1) {
                Questiolist.clear();
                mAlertDialog.dismiss();
                Commans.progressDialog.dismiss();

            }
        }

    }


    private void GetExams(){
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData").child(CategoryName).child(CourseID).child("Exams");

        adapter=new FirebaseRecyclerAdapter<ExamModel, FileAdapter>(ExamModel.class,R.layout.file_item,FileAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final FileAdapter fileAdapter, ExamModel examModel, final int i) {

                fileAdapter.File.setText("Exam "+(i+1)+"\n");

                fileAdapter.File.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_create_black_24dp, 0, 0, 0);

                databaseReference = FirebaseDatabase.getInstance().getReference("UserExamScors").child(Commans.registerModel.getEmail().replace(".", "Dot")).child("ExamsScors");
                databaseReference.child(adapter.getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            ExamScoreModel examScoreModel=dataSnapshot.getValue(ExamScoreModel.class);
                            if (examScoreModel.getState().equals("faild")){
                                fileAdapter.File.setEnabled(false);
                                fileAdapter.File.setText("\n \t You faild in this exam.Try again later");

                            }else {

                                fileAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(Exams.this,ExamQuestion.class);
                                        intent.putExtra("courseID",CourseID);
                                        intent.putExtra("categoryName",CategoryName);
                                        intent.putExtra("ExamID",adapter.getRef(i).getKey());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }else {

                            fileAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(Exams.this,ExamQuestion.class);
                                    intent.putExtra("courseID",CourseID);
                                    intent.putExtra("categoryName",CategoryName);
                                    intent.putExtra("ExamID",adapter.getRef(i).getKey());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        };
        recyclerexam.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerexam.setAdapter(adapter);

    }
    private void initView() {
        recyclerexam = (RecyclerView) findViewById(R.id.recyclerexam);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
