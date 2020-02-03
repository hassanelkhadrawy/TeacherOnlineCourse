package com.example.teacheronlinecourse.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.R;

public class CoursesAdapter extends RecyclerView.ViewHolder {
    public ImageView courseImage;
    public TextView CourseName;
    public TextView CourseChaptersNum;
    public RatingBar ratingBar;
    public CoursesAdapter(@NonNull View itemView) {
        super(itemView);
        courseImage=itemView.findViewById(R.id.imagecourse);
        CourseName=itemView.findViewById(R.id.courseName);
        CourseChaptersNum=itemView.findViewById(R.id.numCourseChapter);
        ratingBar=itemView.findViewById(R.id.ratingBar);

    }
}
