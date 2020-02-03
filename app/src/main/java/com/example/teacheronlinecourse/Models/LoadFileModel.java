package com.example.teacheronlinecourse.Models;

import android.net.Uri;

public class LoadFileModel {
    boolean video;
    boolean image;
    Uri uri;

    public LoadFileModel(boolean video, boolean image, Uri uri) {
        this.video = video;
        this.image = image;
        this.uri = uri;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
