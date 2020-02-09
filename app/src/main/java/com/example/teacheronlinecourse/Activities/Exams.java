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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.AddExamAdapter;
import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.ExamModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Exams extends AppCompatActivity {

    private RecyclerView recyclerAadExam;
    private EditText questiontxt;
    private EditText answertxt;
    private TextView finalexam;
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
        answertxt = (EditText) view.findViewById(R.id.answertxt);
        finalexam = (TextView) view.findViewById(R.id.finalexam);
        builder.setView(view);


        finalexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(questiontxt.getText().toString())) {
                    Toast.makeText(Exams.this, "Add Question", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(answertxt.getText().toString())) {

                    Toast.makeText(Exams.this, "Add Answer", Toast.LENGTH_SHORT).show();

                } else {
                    Questiolist.add(new ExamModel(questiontxt.getText().toString(), answertxt.getText().toString()));
                    AddExamAdapter addExamAdapter = new AddExamAdapter(Exams.this, Questiolist);
                    recyclerAadExam.setLayoutManager(new LinearLayoutManager(Exams.this, LinearLayoutManager.VERTICAL, false));
                    recyclerAadExam.setAdapter(addExamAdapter);
                    questiontxt.setText("");
                    answertxt.setText("");


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
            ExamModel examModel = new ExamModel(Questiolist.get(i).getQuestion(), Questiolist.get(i).getAnswer());
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
            protected void populateViewHolder(FileAdapter fileAdapter, ExamModel examModel, final int i) {

                fileAdapter.File.setText("Exam "+(i+1)+"\n");

                fileAdapter.File.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_create_black_24dp, 0, 0, 0);

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
        };
        recyclerexam.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerexam.setAdapter(adapter);

    }
    private void initView() {
        recyclerexam = (RecyclerView) findViewById(R.id.recyclerexam);
    }
}
