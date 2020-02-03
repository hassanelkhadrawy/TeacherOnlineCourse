package com.example.teacheronlinecourse.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Models.LoadFileModel;
import com.example.teacheronlinecourse.R;

import java.util.ArrayList;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.adapter> {


    class adapter extends RecyclerView.ViewHolder {
        ImageView addImage,deleteImage;

        public adapter(@NonNull View itemView) {
            super(itemView);

            addImage=itemView.findViewById(R.id.imageitem);
            deleteImage=itemView.findViewById(R.id.deleteimageitem);

        }


    }

    Context context;
    ArrayList<LoadFileModel> uriArrayList;

    public AddImageAdapter(Context context, ArrayList<LoadFileModel> uriArrayList) {
        this.context = context;
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.addimageitem,parent,false);
        return new adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter holder, final int position) {

        if (uriArrayList.get(position).isImage()){

            holder.addImage.setImageURI(uriArrayList.get(position).getUri());

        } else if (uriArrayList.get(position).isVideo()) {
            holder.addImage.setBackgroundResource(R.drawable.ic_ondemand_video_black_24dp);

        }else {
            holder.addImage.setBackgroundResource(R.drawable.pdfse);

        }



        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriArrayList.remove(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }


}
