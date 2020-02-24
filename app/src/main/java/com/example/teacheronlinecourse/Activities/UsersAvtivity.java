package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.CoursesAdapter;
import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Models.CourseModel;
import com.example.teacheronlinecourse.Models.RegisterModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersAvtivity extends AppCompatActivity {

    private int Count=0;
    private TextView UsersNumber;
    private RecyclerView userRecycler;
    private FirebaseRecyclerAdapter<RegisterModel, FileAdapter> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_avtivity);
        initView();
    }

    private void initView() {
        userRecycler = (RecyclerView) findViewById(R.id.userRecycler);
        UsersNumber=findViewById(R.id.usersNumber);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        adapter=new FirebaseRecyclerAdapter<RegisterModel, FileAdapter>(RegisterModel.class,R.layout.file_item,FileAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(FileAdapter fileAdapter, RegisterModel registerModel, int i) {

                UsersNumber.setText((i+1)+" User");

                fileAdapter.File.setText("Name: "+registerModel.getName()+"\n"+"Email: "+registerModel.getEmail()+"\n");

                fileAdapter.File.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_perm_identity_black_24dp, 0, 0, 0);



            }
        };
        adapter.notifyDataSetChanged();
        userRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        userRecycler.setAdapter(adapter);
        RemoveUser(adapter,userRecycler);

    }

    private void RemoveUser(final FirebaseRecyclerAdapter<RegisterModel, FileAdapter> adapter, RecyclerView userRecycler){

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                RemoveUserAlert(adapter,position);


            }
        });
        helper.attachToRecyclerView(userRecycler);



    }

    private void RemoveUserAlert(final FirebaseRecyclerAdapter<RegisterModel, FileAdapter> adapter, final int  position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(UsersAvtivity.this);
        builder.setMessage("Do you wnat to delete this user ?");

        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
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
                        adapter.getRef(position).removeValue();

                        mAlertDialog.dismiss();


                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background_withborder);


    }


}
