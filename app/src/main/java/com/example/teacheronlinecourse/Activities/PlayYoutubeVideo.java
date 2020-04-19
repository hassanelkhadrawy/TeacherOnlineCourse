package com.example.teacheronlinecourse.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.teacheronlinecourse.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayYoutubeVideo extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube_video);
        savedInstanceState=getIntent().getExtras();

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        PlayingVideo(savedInstanceState.getString("videoUrl"));
        youTubePlayerView.initialize("AIzaSyDjhsWpcTgvUzmoAUP5pwzI7ZeuJWbVuhY", onInitializedListener);
    }
    private void PlayingVideo(final String Url) {

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


                youTubePlayer.loadVideo(Url);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
