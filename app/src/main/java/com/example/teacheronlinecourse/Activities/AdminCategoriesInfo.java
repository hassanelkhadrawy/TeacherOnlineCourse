package com.example.teacheronlinecourse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminCategoriesInfo extends AppCompatActivity {

    private TextView categoryNumber;
    private RecyclerView adminCategory;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<CategoryModel, FileAdapter> categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories_info);
        //Commans.GetUsers(this);
        initView();
        GetCategoryInfo();
    }

    private void initView() {
        categoryNumber = (TextView) findViewById(R.id.category_Number);
        adminCategory = (RecyclerView) findViewById(R.id.admin_category);
    }
    private void GetCategoryInfo() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

   categoryAdapter=new FirebaseRecyclerAdapter<CategoryModel, FileAdapter>(CategoryModel.class,R.layout.file_item,FileAdapter.class,databaseReference) {
       @Override
       protected void populateViewHolder(final FileAdapter fileAdapter, final CategoryModel categoryModel, final int i) {
           databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(categoryModel.getName());
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()){
                       fileAdapter.File.setText("\n"+categoryModel.getName() + "   "+ dataSnapshot.getChildrenCount() + " course \n");
                   }else {
                       fileAdapter.File.setText("\n"+categoryModel.getName() + " Not found courses \n");

                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
           categoryNumber.setText(categoryAdapter.getItemCount() +" Category");


           fileAdapter.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent intent=new Intent(AdminCategoriesInfo.this,AdminCoursesInfo.class);
                   intent.putExtra("category",categoryModel.getName());
                   startActivity(intent);

               }
           });
       }
   };
        categoryAdapter.notifyDataSetChanged();
        adminCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adminCategory.setAdapter(categoryAdapter);

    }

}
