package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;

public class FileAdapter extends RecyclerView.ViewHolder {
    public TextView File;
    public FileAdapter(@NonNull View itemView) {
        super(itemView);
        File=itemView.findViewById(R.id.file);
    }
}
