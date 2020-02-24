package com.example.teacheronlinecourse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacheronlinecourse.Models.ExamModel;
import com.example.teacheronlinecourse.R;

import java.util.ArrayList;

public class AddExamAdapter extends RecyclerView.Adapter<AddExamAdapter.Viewholder> {






    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView questionNum;
        private TextView question;
        private TextView answer_1;
        private TextView answer_2;
        private TextView answer_3;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
         void initView(View view) {
            questionNum = (TextView)itemView. findViewById(R.id.question_num);
            question = (TextView) itemView.findViewById(R.id.question);
             answer_1 = (TextView)itemView. findViewById(R.id.answer_num_1);
             answer_2 = (TextView)itemView. findViewById(R.id.answer_num_2);
             answer_3 = (TextView)itemView. findViewById(R.id.answer_num_3);

        }
    }

    Context context;
    private ArrayList<ExamModel> Questiolist;

    public AddExamAdapter(Context context, ArrayList<ExamModel> questiolist) {
        this.context = context;
        Questiolist = questiolist;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_question_item, parent, false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.questionNum.setText("Q"+(position+1)+" ");
        holder.question.setText(Questiolist.get(position).getQuestion());
        holder.answer_1.setText(Questiolist.get(position).getAnswer_1());
        holder.answer_2.setText(Questiolist.get(position).getWrong_answer_2());
        holder.answer_3.setText(Questiolist.get(position).getWrong_answer_3());


    }

    @Override
    public int getItemCount() {
        return Questiolist.size();
    }

//    private void DeleteItem(){
//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                int position = viewHolder.getAdapterPosition();
//                recyclerAdapter.getRef(position).removeValue();
//
//            }
//        });
//        helper.attachToRecyclerView(coorsesRecycler);
//
//
//    }

}
