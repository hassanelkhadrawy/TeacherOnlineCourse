package com.example.teacheronlinecourse.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.ExamModel;
import com.example.teacheronlinecourse.Models.ExamScoreModel;
import com.example.teacheronlinecourse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ExamQuestion extends AppCompatActivity {

    private TextView questiontext;
    private RadioGroup radiogroub;
    private RadioButton radioans1;
    private RadioButton radioans2;
    private RadioButton radioans3;
    private RadioButton publicradioButton;
    private Button next;
    private String ExamID,CourseID, CategoryName;
    private DatabaseReference databaseReference;
    ArrayList<ExamModel> examList = new ArrayList<>();
    private LinearLayout countainerquestions;
    private Button start;
    int COUNT = 0;
    int Score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_question);
        savedInstanceState = getIntent().getExtras();
        CategoryName = savedInstanceState.getString("categoryName");
        CourseID = savedInstanceState.getString("courseID");
        ExamID = savedInstanceState.getString("ExamID");
        initView();
        GetQuestions();
    }

    private void initView() {
        countainerquestions = (LinearLayout) findViewById(R.id.countainerquestions);
        questiontext = (TextView) findViewById(R.id.questiontext);
        radiogroub = (RadioGroup) findViewById(R.id.radiogroub);
        radioans1 = (RadioButton) findViewById(R.id.radioans1);
        radioans2 = (RadioButton) findViewById(R.id.radioans2);
        radioans3 = (RadioButton) findViewById(R.id.radioans3);
        next = (Button) findViewById(R.id.next);
        start = (Button) findViewById(R.id.start);
        countainerquestions.setVisibility(View.GONE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countainerquestions.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);

                Random rand = new Random();
                int a = rand.nextInt(2 - 0);
                int Answer2 = rand.nextInt(examList.size() - 0);
                int Answer3 = rand.nextInt(examList.size() - 0);
                if (!examList.isEmpty()) {
                    if (Answer2 != 0 && Answer3 != 0) {
                    Question(a,Answer2,Answer3);

                    }else {
                        Answer2 = rand.nextInt(examList.size() - 0);
                        Answer3 = rand.nextInt(examList.size() - 0);
                        Question(a,Answer2,Answer3);

                    }

                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicradioButton=findViewById(radiogroub.getCheckedRadioButtonId());


                if (examList.get(COUNT).getAnswer().equals(publicradioButton.getText().toString())){

                    Score++;
                }
                if (publicradioButton.isChecked()){
                    publicradioButton.setChecked(false);

                }

                COUNT++;
                if (COUNT!=examList.size()){
                    Random rand = new Random();
                    int a = rand.nextInt(2 - 0);
                    int Answer2 = rand.nextInt(examList.size() - 0);
                    int Answer3 = rand.nextInt(examList.size() - 0);

                    if (Answer2 != COUNT && Answer3 != COUNT) {
                        Question(a,Answer2,Answer3);

                    }else {
                        Answer2 = rand.nextInt(examList.size() - 0);
                        Answer3 = rand.nextInt(examList.size() - 0);
                        Question(a,Answer2,Answer3);

                    }
                }else {
                    next.setVisibility(View.GONE);
                    SaveScore();
                    questiontext.setText( Score +" from "+ examList.size() );
                    radiogroub.setVisibility(View.GONE);
                }

            }
        });

    }

    private void Question(int a,int Answer2,int Answer3){
        questiontext.setText(examList.get(COUNT).getQuestion());

        if (a == 0) {

            radioans1.setText(examList.get(COUNT).getAnswer());
            radioans2.setText(examList.get(Answer2).getAnswer());
            radioans3.setText(examList.get(Answer3).getAnswer());
        } else if (a == 1) {


            radioans1.setText(examList.get(Answer2).getAnswer());
            radioans2.setText(examList.get(COUNT).getAnswer());
            radioans3.setText(examList.get(Answer3).getAnswer());


        } else if (a == 2) {
            radioans1.setText(examList.get(Answer2).getAnswer());
            radioans2.setText(examList.get(Answer3).getAnswer());
            radioans3.setText(examList.get(COUNT).getAnswer());

        }
    }

    private void GetQuestions() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData").child(CategoryName).child(CourseID).child("Exams");

        databaseReference.child(ExamID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        ExamModel examModel = questionSnapshot.getValue(ExamModel.class);
                        examList.add(new ExamModel(examModel.getQuestion(), examModel.getAnswer()));
                    }
                }else {
                    Toast.makeText(ExamQuestion.this, "error", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExamQuestion.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void SaveScore(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCoursesModel");
        ExamScoreModel examScoreModel=new ExamScoreModel(CategoryName,CourseID,ExamID, Score +" from "+ examList.size() );
        databaseReference.child(Commans.registerModel.getEmail().replace(".", "Dot")).child("ExamsScors").child(String.valueOf(System.currentTimeMillis())).setValue(examScoreModel);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
