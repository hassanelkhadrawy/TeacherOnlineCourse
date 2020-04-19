package com.example.teacheronlinecourse.Activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.teacheronlinecourse.Models.SearchModel;
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
    private AutoCompleteTextView searchText;
    private ImageButton searh;
    private RecyclerView searchRecycler;
    private FirebaseRecyclerAdapter<CourseModel, CoursesAdapter> adapter;
    private DatabaseReference databaseReference;
    private ArrayList<SearchModel> coursesList = new ArrayList<>();
    private ArrayList<String> courses_name_list = new ArrayList<>();
    private ArrayList<String> category_name_list = new ArrayList<>();
    private ArrayAdapter<String> adapterCategory;
    private boolean Flage;


    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        AddcoursenameToList();
        Action();


        return view;
    }

    private void initView(View view) {
        searchSpinner = (Spinner) view.findViewById(R.id.searchSpinner);
        searchText = (AutoCompleteTextView) view.findViewById(R.id.searchText);
        searh = (ImageButton) view.findViewById(R.id.searh);
        searchRecycler = (RecyclerView) view.findViewById(R.id.searchRecycler);
    }

    private void Action() {
        searh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flage=true;

                Search(searchText.getText().toString(), searchSpinner.getSelectedItemPosition());

            }
        });
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);

                int pos = -1;

                for (int i = 0; i < courses_name_list.size(); i++) {
                    if (courses_name_list.get(i).equals(selection)) {
                        pos = i;
                        break;
                    }
                }
                Flage=false;
                Search(searchText.getText().toString(), pos);
            }
        });


    }

    private void Search(String searchtext, final int position) {


       if (Flage){
           databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(searchSpinner.getSelectedItem().toString());

       }else {
           databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(coursesList.get(position).getCategory_name());

       }

        final Query firebaseSearchQuery = databaseReference.orderByChild("course_name").startAt(searchtext).endAt(searchtext + "\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    GetSearchResult(firebaseSearchQuery, searchSpinner.getSelectedItem().toString());
                } else {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetSearchResult(Query firebaseSearchQuery, final String CategoryNAme) {


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
                databaseReference2.child(CategoryNAme).child(adapter.getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
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


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Commans.FavouriteFunction(databaseReference, coursesAdapter, courseModel.getCourse_id());


                coursesAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CourseInformation.class);
                        intent.putExtra("categoryName", CategoryNAme);
                        intent.putExtra("courseID", courseModel.getCourse_id());
                        startActivity(intent);

                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchRecycler.setAdapter(adapter);
    }

    private void AddcoursenameToList() {

        courses_name_list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("Search");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        SearchModel searchModel = categorySnapshot.getValue(SearchModel.class);
                        courses_name_list.add(searchModel.getCourse_name());
                        coursesList.add(new SearchModel(searchModel.getCategory_name(), searchModel.getCoueseID(), searchModel.getCourse_name()));
                    }
                    AddListToAdapter(courses_name_list);
                    searchText.setThreshold(1);
                    searchText.setAdapter(adapterCategory);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Category");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                    CategoryModel categoryModel = categorySnapshot.getValue(CategoryModel.class);
                                    category_name_list.add(categoryModel.getName());

                                }

                                AddListToAdapter(category_name_list);
                                searchSpinner.setAdapter(adapterCategory);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });







                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    private void AddListToAdapter(ArrayList<String> list) {

        try {
            adapterCategory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = view.findViewById(android.R.id.text1);
                    tv.setTextColor(Color.parseColor("#C3464444"));
                    return view;
                }
            };
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }


    }
}
