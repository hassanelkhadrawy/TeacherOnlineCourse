package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;
import com.google.android.material.chip.Chip;

public class CategoryAdapter extends RecyclerView.ViewHolder {
    public Chip Category;
    public CategoryAdapter(@NonNull View itemView) {
        super(itemView);
        Category=itemView.findViewById(R.id.chip);
    }
}
