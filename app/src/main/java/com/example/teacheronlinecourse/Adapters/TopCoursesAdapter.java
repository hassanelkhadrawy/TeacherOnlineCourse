package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;

public class TopCoursesAdapter extends RecyclerView.ViewHolder {
    public TextView TopText;
    public RecyclerView topRecycler;
    public TopCoursesAdapter(@NonNull View itemView) {
        super(itemView);
        TopText=itemView.findViewById(R.id.topcourse);
        topRecycler=itemView.findViewById(R.id.tobcourse_recycler);
    }
}
