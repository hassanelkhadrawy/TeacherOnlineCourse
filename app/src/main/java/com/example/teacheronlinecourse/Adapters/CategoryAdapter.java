package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;

public class CategoryAdapter extends RecyclerView.ViewHolder {
    public TextView Category;
    public CategoryAdapter(@NonNull View itemView) {
        super(itemView);
        Category=itemView.findViewById(R.id.categorytext);
    }
}
