package com.example.teacheronlinecourse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teacheronlinecourse.Activities.Courses;
import com.example.teacheronlinecourse.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        switch (position) {
            case 0:

                Picasso.with(context).load("https://firebasestorage.googleapis.com/v0/b/teacher-online-course.appspot.com/o/computer-network-networking-austin-833x474.jpg?alt=media&token=930bb3ad-4946-4676-8952-9c87b4e43993").placeholder(R.drawable.ic_perm_identity_black_24dp).into(viewHolder.imageViewBackground);

                break;
            case 1:

                Picasso.with(context).load("https://firebasestorage.googleapis.com/v0/b/teacher-online-course.appspot.com/o/cybersecurity-100635851-orig.jpg?alt=media&token=fe8535f7-7b40-480d-a84f-65b3c2fa6397").placeholder(R.drawable.ic_perm_identity_black_24dp).into(viewHolder.imageViewBackground);

                break;
            case 2:

                Picasso.with(context).load("https://firebasestorage.googleapis.com/v0/b/teacher-online-course.appspot.com/o/images%20(2).jpeg?alt=media&token=04df922d-2894-4ae0-b454-9d906190d3b5").placeholder(R.drawable.ic_perm_identity_black_24dp).into(viewHolder.imageViewBackground);

                break;
            default:

                Picasso.with(context).load("https://firebasestorage.googleapis.com/v0/b/teacher-online-course.appspot.com/o/sicurezza-aziendale-1.jpg?alt=media&token=15b3ea1e-0a97-4822-b295-567cebb736bd").placeholder(R.drawable.ic_perm_identity_black_24dp).into(viewHolder.imageViewBackground);

                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 4;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}