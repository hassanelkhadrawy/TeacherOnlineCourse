package com.example.teacheronlinecourse.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.FAvouriteModel;
import com.example.teacheronlinecourse.Models.RateModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteCourses extends Fragment {


    private RecyclerView favouriteRecycler;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<FAvouriteModel, CoursesAdapter> recyclerAdapter;

    public FavouriteCourses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourite_courses, container, false);
        initView(view);
        GetFivouriteCourses();

        return view;
    }

    private void initView(View view) {
        favouriteRecycler = (RecyclerView)view. findViewById(R.id.favourite_recycler);
    }
    private void GetFivouriteCourses(){

        databaseReference= FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".","Dot"));
        recyclerAdapter=new FirebaseRecyclerAdapter<FAvouriteModel, CoursesAdapter>(FAvouriteModel.class,R.layout.courses_item,CoursesAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final CoursesAdapter coursesAdapter, final FAvouriteModel fAvouriteModel, final int i) {

                if (!fAvouriteModel.getCourse_image().equals("null")) {
                    Picasso.with(getActivity()).load(fAvouriteModel.getCourse_image()).placeholder(R.drawable.ic_perm_identity_black_24dp).into(coursesAdapter.courseImage);
                } else {
                    coursesAdapter.courseImage.setVisibility(View.GONE);

                }

                    coursesAdapter.CourseName.setText(fAvouriteModel.getCourse_name());

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("CoursesRates");
                    databaseReference2.child(fAvouriteModel.getCourse_category()).child(recyclerAdapter.getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
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

                    FavouriteFunction(coursesAdapter,i);
                    saveState(false);





                coursesAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CourseInformation.class);
                        intent.putExtra("categoryName", fAvouriteModel.getCourse_category());
                        intent.putExtra("courseID", recyclerAdapter.getRef(i).getKey());
                        startActivity(intent);

                    }
                });


                coursesAdapter.favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getActivity(), R.string.unfavorite, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        };
        recyclerAdapter.notifyDataSetChanged();
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        favouriteRecycler.setAdapter(recyclerAdapter);

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
        helper.attachToRecyclerView(favouriteRecycler);


    }


    private void FavouriteFunction(final CoursesAdapter coursesAdapter ,int i){
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".", "Dot")).child(recyclerAdapter.getRef(i).getKey());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FAvouriteModel fAvouriteModel = dataSnapshot.getValue(FAvouriteModel.class);
                    if (fAvouriteModel.isFavourite()) {
                        coursesAdapter.favourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    } else {
                        coursesAdapter.favourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void saveState(boolean isFavourite) {
        SharedPreferences aSharedPreferences = getActivity().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.commit();
    }



}
