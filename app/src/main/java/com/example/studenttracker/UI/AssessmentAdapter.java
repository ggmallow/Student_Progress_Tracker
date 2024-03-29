package com.example.studenttracker.UI;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.MyViewHolder> {

    private ArrayList<Assessment> allAssessmentList;
    private assessmentClickListener mAssessmentClickListener;
    public int checkedPosition = -1;



    public AssessmentAdapter(ArrayList<Assessment> allAssessmentList, assessmentClickListener assessmentClickListener) {
        this.allAssessmentList = allAssessmentList;
        this.mAssessmentClickListener = assessmentClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView assessmentNameText;
        assessmentClickListener assessmentClickListener;

        public MyViewHolder(final View view, assessmentClickListener assessmentClickListener) {
            super(view);
            assessmentNameText = view.findViewById(R.id.assessmentName);
            this.assessmentClickListener = assessmentClickListener;

            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            try {
                mAssessmentClickListener.onAssessmentClick(getAdapterPosition());
            } catch (Exception e) {
                Log.println(Log.INFO,"debug", "Null click");

            }


        }


    }

    public interface assessmentClickListener {

        void onAssessmentClick(int  position);

    }


    @NonNull
    @Override
    public AssessmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment, parent, false);


        return new MyViewHolder(itemView, mAssessmentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.MyViewHolder holder, int position) {


        String assessmentName = allAssessmentList.get(position).getAssessmentTitle();
        holder.assessmentNameText.setText(assessmentName);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();

            }
        } );


        if (checkedPosition == position) {
            holder.assessmentNameText.setBackgroundColor(Color.parseColor("#FF018786"));
            mAssessmentClickListener.onAssessmentClick(holder.getAdapterPosition());


        } else {
            holder.assessmentNameText.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }


    }

    @Override
    public int getItemCount() {
        return allAssessmentList.size();
    }
}
