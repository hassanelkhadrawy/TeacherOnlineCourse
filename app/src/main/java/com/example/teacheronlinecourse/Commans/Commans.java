package com.example.teacheronlinecourse.Commans;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Models.AdminModel;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.FAvouriteModel;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Commans {
    public static ProgressDialog progressDialog;
   public static RegisterModel registerModel;
   public static ArrayList<String>adminList=new ArrayList<>();

    public static void Prograss(Context context,String Message){
        progressDialog=new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(Message);
        progressDialog.setCancelable(false);

    }

    //for checking connection internet
    public static boolean isConnectToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


    public static void FavouriteFunction(DatabaseReference databaseReference, final CoursesAdapter coursesAdapter , String CourseID){
        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesFavourite").child(Commans.registerModel.getEmail().replace(".", "Dot")).child(CourseID);
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

                }else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public static void GetAdmins(DatabaseReference databaseReference){

        databaseReference=FirebaseDatabase.getInstance().getReference("Admins");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot adminAnapshot : dataSnapshot.getChildren()){
                    AdminModel adminModel=adminAnapshot.getValue(AdminModel.class);
                    adminList.add(adminModel.getAdmin_email());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static void hidenAdd(Menu menu){
        MenuItem item = menu.findItem(R.id.add_item);
        for (int i= 0;i<adminList.size();i++){
            if (registerModel.getEmail().equals(adminList.get(i))){
                item.setVisible(true);

            }else {
                item.setVisible(false);
            }
        }


    }
    public static void RemoveCourse(final FirebaseRecyclerAdapter<CourseModel, CoursesAdapter> recyclerAdapter, RecyclerView coorsesRecycler){
        for (int i= 0;i<Commans.adminList.size();i++){

            if (Commans.registerModel.getEmail().equals(Commans.adminList.get(i))){
                ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();
                        recyclerAdapter.getRef(position).removeValue();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Search");
                        databaseReference.child(recyclerAdapter.getRef(position).getKey()).removeValue();

                    }
                });
                helper.attachToRecyclerView(coorsesRecycler);
            }
        }

    }
}
