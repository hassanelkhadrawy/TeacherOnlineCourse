package com.example.teacheronlinecourse.Activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.CategoryModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {


    private Spinner searchSpinner;
    private Comfortaa_Regular searchText;
    private ImageButton searh;
    private RecyclerView searchRecycler;
    private FirebaseRecyclerAdapter<CourseModel, CoursesAdapter> adapter;
    private DatabaseReference databaseReference;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> adapterCategory;


    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        AddCAtegoryToList();
        Action();


        return view;
    }

    private void initView(View view) {
        searchSpinner = (Spinner) view.findViewById(R.id.searchSpinner);
        searchText = (Comfortaa_Regular) view.findViewById(R.id.searchText);
        searh = (ImageButton) view.findViewById(R.id.searh);
        searchRecycler = (RecyclerView) view.findViewById(R.id.searchRecycler);
    }

    private void Action() {
        searh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search(searchText.getText().toString());
            }
        });


    }

    private void Search(String searchtext) {
        Commans.progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(searchSpinner.getSelectedItem().toString());
        final Query firebaseSearchQuery = databaseReference.orderByChild("course_name").startAt(searchtext).endAt(searchtext + "\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){

                    GetSearchResult(firebaseSearchQuery);
                }else {
                    Commans.progressDialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.not_found), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetSearchResult(Query firebaseSearchQuery){
        adapter = new FirebaseRecyclerAdapter<CourseModel, CoursesAdapter>(CourseModel.class, R.layout.courses_item, CoursesAdapter.class, firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(final CoursesAdapter coursesAdapter, final CourseModel courseModel, final int i) {



                if (!courseModel.getCourse_image().equals("null")) {
                    Picasso.with(getActivity()).load(courseModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
                } else {
                    coursesAdapter.courseImage.setVisibility(View.GONE);

                }
                coursesAdapter.CourseName.setText(courseModel.getCourse_name());


                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("CoursesRates");
                databaseReference2.child(searchSpinner.getSelectedItem().toString()).child(adapter.getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        RateModel rateModel;
                        float rate = 0;

                        for (DataSnapshot rateSnapshot : dataSnapshot.getChildren()) {
                            rateModel = rateSnapshot.getValue(RateModel.class);
                            rate += rateModel.getRate();
                        }

                        rate = rate / dataSnapshot.getChildrenCount();
                        coursesAdapter.ratingBar.setRating(rate);
                        Commans.progressDialog.dismiss();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Commans.progressDialog.dismiss();

                    }
                });


                coursesAdapter.courseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CourseInformation.class);
                        intent.putExtra("categoryName", searchSpinner.getSelectedItem().toString());
                        intent.putExtra("courseID", adapter.getRef(i).getKey());
                        intent.putExtra("courseImage", courseModel.getCourse_image());
                        startActivity(intent);

                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchRecycler.setAdapter(adapter);
    }
    private void AddCAtegoryToList() {
        Commans.Prograss(getActivity(), getString(R.string.waiting));
        Commans.progressDialog.show();
        categoryList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("Category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        CategoryModel categoryModel = categorySnapshot.getValue(CategoryModel.class);
                        categoryList.add(categoryModel.getName());
                    }

                    adapterCategory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoryList) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView tv = view.findViewById(android.R.id.text1);
                            tv.setTextColor(Color.parseColor("#C3464444"));
                            return view;
                        }
                    };
                    searchSpinner.setAdapter(adapterCategory);

                    Commans.progressDialog.dismiss();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Commans.progressDialog.dismiss();

            }
        });


    }

}
