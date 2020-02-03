package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;

public class FileAdapter extends RecyclerView.ViewHolder {
    public ImageView FileImage;
    public FileAdapter(@NonNull View itemView) {
        super(itemView);
        FileImage=itemView.findViewById(R.id.file_image);
    }
}
