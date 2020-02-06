package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.AddImageAdapter;
import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Models.FileModel;
import com.example.teacheronlinecourse.Models.LoadFileModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class Files extends AppCompatActivity {

    private LinearLayout addItemContainer;
    private TextView phototext;
    private RecyclerView addImageRecycler;
    private TextView Addphoto;
    private TextView Addfile;
    private TextView Addvideo;
    private Comfortaa_Bold cancleupload;
    private Comfortaa_Bold upLoadItem;
    private ArrayList<LoadFileModel> uriArrayList;
    private Uri saveuri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String courseID, categoryName, chapterID;
    private LinearLayout countainer;
    private RecyclerView recyclerFiles;
    private FirebaseRecyclerAdapter<FileModel, FileAdapter> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        savedInstanceState = getIntent().getExtras();
        courseID = savedInstanceState.getString("courseID");
        categoryName = savedInstanceState.getString("categoryName");
        chapterID = savedInstanceState.getString("chapterID");
        initView();
        GetFiles();
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

            AddItem();
        }

        return super.onOptionsItemSelected(item);
    }

    public void SelectImage() {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);

    }
    public void SelectVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 3);

    }

    public void SelectFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "select file"), 2);

    }

    private void AddItem() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_file_item, null);

        addItemContainer = (LinearLayout) view.findViewById(R.id.addItemContainer);
        phototext = (TextView) view.findViewById(R.id.phototext);
        addImageRecycler = (RecyclerView) view.findViewById(R.id.addImageRecycler);
        Addphoto = (TextView) view.findViewById(R.id._addphoto);
        Addfile = (TextView) view.findViewById(R.id._adddfile);
        Addvideo=(TextView)view.findViewById(R.id._addvideo);
        cancleupload = (Comfortaa_Bold) view.findViewById(R.id.cancleupload);
        upLoadItem = (Comfortaa_Bold) view.findViewById(R.id.upLoadItem);

        Commans.Prograss(this, getString(R.string.waiting));


        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        Addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        Addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectFile();
            }
        });

        Addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             SelectVideo();
            }
        });

        upLoadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadItemData(uriArrayList, categoryName, dialog);
            }
        });
        cancleupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.button_background);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                uriArrayList = new ArrayList<>();
                uriArrayList.clear();
                ClipData clipData = data.getClipData();
                if (clipData != null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        saveuri = clipData.getItemAt(i).getUri();

                        uriArrayList.add(new LoadFileModel(false,true,saveuri));

                    }


                } else {


                    saveuri = data.getData();
                    uriArrayList.add(new LoadFileModel(false,true,saveuri));


                }


            } else if (requestCode == 2) {
                uriArrayList = new ArrayList<>();
                uriArrayList.clear();
                ClipData clipData = data.getClipData();
                if (clipData != null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        saveuri = clipData.getItemAt(i).getUri();


                        uriArrayList.add(new LoadFileModel(false,false,saveuri));

                    }


                } else {


                    saveuri = data.getData();
                    uriArrayList.add(new LoadFileModel(false,false,saveuri));


                }

            }else if (requestCode == 3) {
                uriArrayList = new ArrayList<>();
                uriArrayList.clear();
                ClipData clipData = data.getClipData();
                if (clipData != null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        saveuri = clipData.getItemAt(i).getUri();


                        uriArrayList.add(new LoadFileModel(true,false,saveuri));

                    }


                } else {


                    saveuri = data.getData();
                    uriArrayList.add(new LoadFileModel(true,false,saveuri));


                }

            }

            if (!uriArrayList.isEmpty()) {

                phototext.setVisibility(View.GONE);
                addImageRecycler.setVisibility(View.VISIBLE);
                addImageRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                AddImageAdapter addImageAdapter = new AddImageAdapter(this, uriArrayList);
                addImageRecycler.setAdapter(addImageAdapter);
                addImageAdapter.notifyDataSetChanged();

            }
        }
    }



    public void UploadItemData(final ArrayList<LoadFileModel> uriArrayList, final String catedory, final AlertDialog dialog) {

        Commans.progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData");
        storageReference = FirebaseStorage.getInstance().getReference("images/");

        if (!uriArrayList.isEmpty()) {


            for (int i = 0; i < uriArrayList.size(); i++) {


                String imgName = UUID.randomUUID().toString();
                final StorageReference imageFolder = storageReference.child("images/" + imgName);
                final int finalI = i;
                final int finalI1 = i;
                imageFolder.putFile(uriArrayList.get(i).getUri())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        if (uriArrayList.get(finalI1).isVideo()){
                                            FileModel fileModel = new FileModel(uri.toString(),uriArrayList.get(finalI1).isVideo());

                                            databaseReference.child(categoryName).child(courseID).child(chapterID).child("chpter_data").child("videos").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);


                                        }else {
                                            FileModel fileModel = new FileModel(uri.toString(),uriArrayList.get(finalI1).isImage());

                                            databaseReference.child(categoryName).child(courseID).child(chapterID).child("chpter_data").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);


                                        }



                                        if (finalI == uriArrayList.size() - 1) {
                                            Commans.progressDialog.dismiss();
                                            dialog.dismiss();
                                            Snackbar.make(countainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Commans.progressDialog.dismiss();
                                dialog.dismiss();
                                Snackbar.make(countainer, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        });

            }

        }
    }


    private void initView() {
        countainer = (LinearLayout) findViewById(R.id.countainer);
        recyclerFiles = (RecyclerView) findViewById(R.id.recycler_files);
    }

    private void GetFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData").child(categoryName).child(courseID).child(chapterID).child("chpter_data");
        adapter = new FirebaseRecyclerAdapter<FileModel, FileAdapter>(FileModel.class, R.layout.file_item, FileAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(FileAdapter fileAdapter, final FileModel fileModel, int i) {

                Picasso.with(Files.this).load(fileModel.getFileUrl()).placeholder(R.drawable.pdfse).into(fileAdapter.FileImage);

                fileAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Files.this, WebViewActivity.class);
                        intent.putExtra("url_file", fileModel.getFileUrl());
                        intent.putExtra("flag", fileModel.isFlag());

                        startActivity(intent);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerFiles.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerFiles.setAdapter(adapter);


    }

}