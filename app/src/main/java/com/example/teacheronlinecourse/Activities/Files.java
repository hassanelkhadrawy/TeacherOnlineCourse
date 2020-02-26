package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private RecyclerView addImageRecycler;
    private TextView Addphoto;
    private TextView Addfile;
    private TextView Addvideo;
    private TextView SelectVideo;
    private EditText VideoURL;
    private LinearLayout voideoCountainer;
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
    private RecyclerView recyclerVideos;


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
        GetVideos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        Commans.hidenAdd(menu);
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
        intent.setType("image/*");
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
        addImageRecycler = (RecyclerView) view.findViewById(R.id.addImageRecycler);
        Addphoto = (TextView) view.findViewById(R.id._addphoto);
        Addfile = (TextView) view.findViewById(R.id._adddfile);
        Addvideo = (TextView) view.findViewById(R.id._addvideo);
        cancleupload = (Comfortaa_Bold) view.findViewById(R.id.cancleupload);
        upLoadItem = (Comfortaa_Bold) view.findViewById(R.id.upLoadItem);
        SelectVideo=view.findViewById(R.id._selectvideo);
        VideoURL=view.findViewById(R.id.videoUrl);
        voideoCountainer=view.findViewById(R.id.videoUrlcountainer);

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
                SelectVideo.setVisibility(View.VISIBLE);
                voideoCountainer.setVisibility(View.VISIBLE);
                VideoURL.setVisibility(View.VISIBLE);

                //SelectVideo();
            }
        });

        SelectVideo.setOnClickListener(new View.OnClickListener() {
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

                        uriArrayList.add(new LoadFileModel(false, true, saveuri));

                    }


                } else {


                    saveuri = data.getData();
                    uriArrayList.add(new LoadFileModel(false, true, saveuri));


                }


            } else if (requestCode == 2) {
                uriArrayList = new ArrayList<>();
                uriArrayList.clear();
                ClipData clipData = data.getClipData();
                if (clipData != null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        saveuri = clipData.getItemAt(i).getUri();


                        uriArrayList.add(new LoadFileModel(false, false, saveuri));

                    }


                } else {


                    saveuri = data.getData();
                    uriArrayList.add(new LoadFileModel(false, false, saveuri));


                }

            } else if (requestCode == 3) {
                uriArrayList = new ArrayList<>();
                uriArrayList.clear();
                ClipData clipData = data.getClipData();
                if (clipData != null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        saveuri = clipData.getItemAt(i).getUri();


                        uriArrayList.add(new LoadFileModel(true, false, saveuri));

                    }


                } else {


                    saveuri = data.getData();
                    uriArrayList.add(new LoadFileModel(true, false, saveuri));


                }

            }

            if (!uriArrayList.isEmpty()) {

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

        if (saveuri!=null) {


            for (int i = 0; i < uriArrayList.size(); i++) {


                String imgName = UUID.randomUUID().toString();
                final StorageReference imageFolder = storageReference.child("images/" + imgName);
                final int finalI = i;
                final int finalI1 = i;
                final int finalI2 = i;
                imageFolder.putFile(uriArrayList.get(i).getUri())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        if (uriArrayList.get(finalI1).isVideo()) {
                                            FileModel fileModel = new FileModel(uri.toString(), true);

                                            databaseReference.child(categoryName).child(courseID).child("Chapters").child(chapterID).child("chpter_videos").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);
                                            Snackbar.make(countainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                                        } else {
                                            FileModel fileModel = new FileModel(uri.toString(), uriArrayList.get(finalI2).isImage());

                                            databaseReference.child(categoryName).child(courseID).child("Chapters").child(chapterID).child("chpter_data").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);


                                        }


                                        if (finalI == uriArrayList.size() - 1) {
                                            Commans.progressDialog.dismiss();
                                            dialog.dismiss();
                                            saveuri=null;
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
                                saveuri=null;
                                Snackbar.make(countainer, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        });

            }

        }else {
            if (!TextUtils.isEmpty(VideoURL.getText().toString())){
                FileModel fileModel = new FileModel(VideoURL.getText().toString(), false);
                databaseReference.child(categoryName).child(courseID).child("Chapters").child(chapterID).child("chpter_videos").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);
                Commans.progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, R.string.entervideourl, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initView() {
        countainer = (LinearLayout) findViewById(R.id.countainer);
        recyclerFiles = (RecyclerView) findViewById(R.id.recycler_files);
        recyclerVideos = (RecyclerView) findViewById(R.id.recycler_videos);
    }

    private void GetFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData").child(categoryName).child(courseID).child("Chapters").child(chapterID).child("chpter_data");
        adapter = new FirebaseRecyclerAdapter<FileModel, FileAdapter>(FileModel.class, R.layout.file_item, FileAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(FileAdapter fileAdapter, final FileModel fileModel, int i) {


                fileAdapter.File.setText("File \n");

                fileAdapter.File.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attach_file_black_24dp, 0, 0, 0);

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
        recyclerFiles.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerFiles.setAdapter(adapter);


    }

    private void GetVideos() {


        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData").child(categoryName).child(courseID).child("Chapters").child(chapterID).child("chpter_videos");
        adapter = new FirebaseRecyclerAdapter<FileModel, FileAdapter>(FileModel.class, R.layout.file_item, FileAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(final FileAdapter fileAdapter, final FileModel fileModel, int i) {


                fileAdapter.File.setText("Lecture \n");
                fileAdapter.File.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ondemand_video_black_24dp, 0, 0, 0);

                fileAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (fileModel.isFlag()){
                            Intent intent=new Intent(Files.this,PlayExoPlayerView.class);
                            intent.putExtra("videoUrl",fileModel.getFileUrl());
                            startActivity(intent);


                        }else {


                            Intent intent=new Intent(Files.this,PlayYoutubeVideo.class);
                            intent.putExtra("videoUrl",fileModel.getFileUrl());
                            startActivity(intent);




                        }


                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerVideos.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
