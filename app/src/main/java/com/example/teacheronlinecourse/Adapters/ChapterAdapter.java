package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;

public class ChapterAdapter extends RecyclerView.ViewHolder {
   public TextView ChapterName;
    public ChapterAdapter(@NonNull View itemView) {
        super(itemView);
        ChapterName=itemView.findViewById(R.id.chaptername);
    }
}
