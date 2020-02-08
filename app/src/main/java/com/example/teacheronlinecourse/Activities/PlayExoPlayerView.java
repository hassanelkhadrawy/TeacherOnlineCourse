package com.example.teacheronlinecourse.Activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacheronlinecourse.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class PlayExoPlayerView extends AppCompatActivity {

    private SimpleExoPlayerView exoplayerView;
    private SimpleExoPlayer player;
    private Uri videoURI;
    private MediaSource mediaSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_exo_player_view);
        savedInstanceState=getIntent().getExtras();
        initView();
        Exoplayer(savedInstanceState.getString("videoUrl"));
    }

    private void initView() {
        exoplayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayerView);
    }
    void Exoplayer(String VideoUrl) {

        try {

            if (player != null) {
                player.setPlayWhenReady(false);
                player.stop();
                player.seekTo(0);
                player.release();
            }

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            videoURI = Uri.parse(VideoUrl);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            exoplayerView.setPlayer(player);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);


        } catch (Exception e) {
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
            player.release();
        }

        finish();
    }
}
