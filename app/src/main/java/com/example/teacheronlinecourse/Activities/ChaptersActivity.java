package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.ChapterAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.ChaptersModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ChaptersActivity extends AppCompatActivity {

//    private Comfortaa_Regular addChapterName;
//    private Comfortaa_Bold cancleAddChapter;
//    private Comfortaa_Bold uploadChapter;
//    private DatabaseReference databaseReference;
//    private FirebaseRecyclerAdapter<ChaptersModel, ChapterAdapter> recyclerAdapter;
//    private String courseID, categoryName;
//    private RecyclerView chaptersRecycler;
//    private LinearLayout chaptersCountainer;
//    private TextView finalexam;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chapters);
//        savedInstanceState = getIntent().getExtras();
//        savedInstanceState = getIntent().getExtras();
//        courseID = savedInstanceState.getString("courseID");
//        categoryName = savedInstanceState.getString("categoryName");
//        initView();
//        GetChapters();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.add_item) {
//
//            ChapterDialog();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void ChapterDialog() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.add_chapter_item, null);
//        addChapterName = (Comfortaa_Regular) view.findViewById(R.id.addChapterName);
//        cancleAddChapter = (Comfortaa_Bold) view.findViewById(R.id.cancleAddChapter);
//        uploadChapter = (Comfortaa_Bold) view.findViewById(R.id.uploadChapter);
//
//        Commans.Prograss(this, getString(R.string.waiting));
//
//
//        builder.setView(view);
//        builder.setCancelable(false);
//        final AlertDialog dialog = builder.create();
//
//        uploadChapter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SendChapterData(dialog);
//            }
//        });
//        cancleAddChapter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);
//
//    }
//
//    private void SendChapterData(AlertDialog dialog) {
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("CoursesData");
//        final String chapterID = UUID.randomUUID().toString();
//        ChaptersModel chaptersModel = new ChaptersModel(addChapterName.getText().toString());
//        databaseReference.child(categoryName).child(courseID).child(String.valueOf(System.currentTimeMillis())).setValue(chaptersModel);
//        dialog.dismiss();
//        Snackbar.make(chaptersCountainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
//
//
//    }
//
//
//    private void initView() {
//        chaptersRecycler = (RecyclerView) findViewById(R.id.chaptersRecycler);
//        chaptersCountainer = (LinearLayout) findViewById(R.id.chaptersCountainer);
//        finalexam = (TextView) findViewById(R.id.finalexam);
//        finalexam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(ChaptersActivity.this,Exams.class);
//                intent.putExtra("courseID",courseID);
//                intent.putExtra("categoryName",categoryName);
//                startActivity(intent);
//
//            }
//        });
//    }
//
//    private void GetChapters() {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("CoursesData").child(categoryName).child(courseID);
//        recyclerAdapter = new FirebaseRecyclerAdapter<ChaptersModel, ChapterAdapter>(ChaptersModel.class, R.layout.chapter_item, ChapterAdapter.class, databaseReference) {
//            @Override
//            protected void populateViewHolder(final ChapterAdapter chapterAdapter, ChaptersModel chaptersModel, final int i) {
//                chapterAdapter.ChapterName.setText(chaptersModel.getChapter_name());
//                chapterAdapter.ChapterNum.setText(""+(i+1));
//                chapterAdapter.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        chapterAdapter.ChapterNum.setBackgroundResource(R.drawable.background_fillcircle);
//                        Intent intent = new Intent(ChaptersActivity.this, Files.class);
//                        intent.putExtra("courseID", courseID);
//                        intent.putExtra("categoryName", categoryName);
//                        intent.putExtra("chapterID", recyclerAdapter.getRef(i).getKey());
//                        startActivity(intent);
//                    }
//                });
//            }
//        };
//        recyclerAdapter.notifyDataSetChanged();
//        chaptersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        chaptersRecycler.setAdapter(recyclerAdapter);
//
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
}
