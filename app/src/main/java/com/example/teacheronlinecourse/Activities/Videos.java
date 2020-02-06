package com.example.teacheronlinecourse.Activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Adapters.AddImageAdapter;
import com.example.teacheronlinecourse.Adapters.FileAdapter;
import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Bold;
import com.example.teacheronlinecourse.Fonts.Comfortaa_Regular;
import com.example.teacheronlinecourse.Models.FileModel;
import com.example.teacheronlinecourse.Models.LoadFileModel;
import com.example.teacheronlinecourse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;


public class Videos extends YouTubeBaseActivity {

    private LinearLayout addItemContainer;
    private RecyclerView addImageRecycler;
    private TextView Addvideo;
    private Comfortaa_Bold cancleupload;
    private Comfortaa_Bold upLoadItem;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ArrayList<LoadFileModel> uriArrayList;
    private String courseID, categoryName, chapterID;
    private Uri saveuri;
    private LinearLayout countainer;
    private RecyclerView recyclerVideo;
    private FirebaseRecyclerAdapter<FileModel, FileAdapter> adapter;
    private Comfortaa_Regular videoUrl;
    private ImageButton add;
    Uri videoURI;
//========================================
   private YouTubePlayer globalyouTubePlayer;
    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private SimpleExoPlayerView exoPlayerView;
    private boolean idPlaying=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        savedInstanceState = getIntent().getExtras();
        courseID = savedInstanceState.getString("courseID");
        categoryName = savedInstanceState.getString("categoryName");
        chapterID = savedInstanceState.getString("chapterID");
        initView();
        GetVideos();
    }

    public void SelectVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);

    }


    private void AddItem() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_video_item, null);

        addItemContainer = (LinearLayout) view.findViewById(R.id.addItemContainer);
        addImageRecycler = (RecyclerView) view.findViewById(R.id.addImageRecycler);
        Addvideo = (TextView) view.findViewById(R.id._addvideo);
        videoUrl = (Comfortaa_Regular) view.findViewById(R.id.videoUrl);
        cancleupload = (Comfortaa_Bold) view.findViewById(R.id.cancleupload);
        upLoadItem = (Comfortaa_Bold) view.findViewById(R.id.upLoadItem);

        Commans.Prograss(this, getString(R.string.waiting));


        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        Addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectVideo();
            }
        });

        upLoadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(videoUrl.getText().toString())) {
                    UploadVideoData(uriArrayList, "noting", dialog);
                } else {
                    UploadVideoData(null, videoUrl.getText().toString(), dialog);


                }
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

    private void UploadVideoData(final ArrayList<LoadFileModel> uriArrayList, final String video_url, final AlertDialog dialog) {

        Commans.progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData");
        storageReference = FirebaseStorage.getInstance().getReference("videos/");

        if (uriArrayList != null) {


            for (int i = 0; i < uriArrayList.size(); i++) {


                String imgName = UUID.randomUUID().toString();
                final StorageReference videoFolder = storageReference.child("videos/" + imgName);
                final int finalI = i;
                final int finalI1 = i;
                videoFolder.putFile(uriArrayList.get(i).getUri())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                videoFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {


                                        FileModel fileModel = new FileModel(uri.toString(), uriArrayList.get(finalI1).isVideo());

                                        databaseReference.child(categoryName).child(courseID).child(chapterID).child("chpter_videos").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);


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

        } else {

            FileModel fileModel = new FileModel(video_url, false);

            databaseReference.child(categoryName).child(courseID).child(chapterID).child("chpter_videos").child(String.valueOf(System.currentTimeMillis())).setValue(fileModel);
            Commans.progressDialog.dismiss();
            dialog.dismiss();
            Snackbar.make(countainer, getString(R.string.success), Snackbar.LENGTH_SHORT).show();


        }

    }

    private void initView() {
        countainer = (LinearLayout) findViewById(R.id.countainer);
        recyclerVideo = (RecyclerView) findViewById(R.id.recycler_video);


        add = (ImageButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });
        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayerView);
       // mediaController = new MediaController(this);

    }

    private void GetVideos() {


        databaseReference = FirebaseDatabase.getInstance().getReference("CoursesData").child(categoryName).child(courseID).child(chapterID).child("chpter_videos");
        adapter = new FirebaseRecyclerAdapter<FileModel, FileAdapter>(FileModel.class, R.layout.file_item, FileAdapter.class, databaseReference) {
            @Override
            protected void populateViewHolder(final FileAdapter fileAdapter, final FileModel fileModel, int i) {


                fileAdapter.FileImage.setBackgroundResource(R.drawable.ic_ondemand_video_black_24dp);
                fileAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fileModel.isFlag()){
                           // exoPlayerView.setVisibility(View.VISIBLE);

                            if (player != null) {
                                player.setPlayWhenReady(false);
                                player.stop();
                                player.seekTo(0);
                                Exoplayer(fileModel.getFileUrl());

                            }else {
                                Exoplayer(fileModel.getFileUrl());

                            }

                        }else {
                            if (player != null) {
                                player.setPlayWhenReady(false);
                                player.stop();
                                player.seekTo(0);
                                player.release();
                            }

                            Intent intent=new Intent(Videos.this,PlayYoutubeVideo.class);
                            intent.putExtra("videoUrl",fileModel.getFileUrl());
                            startActivity(intent);




                        }





                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerVideo.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerVideo.setAdapter(adapter);


    }

    void Exoplayer(String VideoUrl) {

        try {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            videoURI = Uri.parse(VideoUrl);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            exoPlayerView.setPlayer(player);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);


        } catch (Exception e) {
        }

    }

    private void onYoutubeClicked(String youtubeUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse(youtubeUrl));
        } catch (PackageManager.NameNotFoundException e) {
            intent.setData(Uri.parse(youtubeUrl));
        } finally {
            startActivity(intent);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
            player.release();
        }    }


}