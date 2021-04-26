package edu.neu.madcourse.musicool;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultGridAdapter extends RecyclerView.Adapter<ResultGridAdapter.MyViewHolder> {

    public List<Question> questionList;
    Context context;

    public ResultGridAdapter(List<Question> questionList, Context context) {
        this.questionList = new ArrayList<>(questionList);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_result_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drawable img;

        holder.btnQuestion.setText(new StringBuilder("Question ").append(position+1));
        if(Result.resultList.get(position)){
            holder.btnQuestion.setBackgroundColor(Color.parseColor("#ff99cc00"));
            img = context.getDrawable(R.drawable.ic_baseline_check_24);
            holder.btnQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
        }else{
            holder.btnQuestion.setBackgroundColor(Color.parseColor("#ff99cc00"));
            img = context.getDrawable(R.drawable.ic_baseline_clear_white_24);
            holder.btnQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button btnQuestion;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnQuestion = itemView.findViewById(R.id.btn_question);
            //todo: set on click listener to check the question
        }

    }
}
