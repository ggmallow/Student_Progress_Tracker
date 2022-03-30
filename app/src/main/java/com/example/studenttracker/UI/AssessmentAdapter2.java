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

public class AssessmentAdapter2 extends RecyclerView.Adapter<AssessmentAdapter2.MyViewHolder> {

    private ArrayList<Assessment> allAssessmentList;
    private assessmentClickListener2 mAssessmentClickListener2;
    public int checkedPosition = -1;



    public AssessmentAdapter2(ArrayList<Assessment> allAssessmentList, assessmentClickListener2 assessmentClickListener2) {
        this.allAssessmentList = allAssessmentList;
        this.mAssessmentClickListener2 = assessmentClickListener2;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView assessmentNameText;
        assessmentClickListener2 assessmentClickListener2;

        public MyViewHolder(final View view, assessmentClickListener2 assessmentClickListener2) {
            super(view);
            assessmentNameText = view.findViewById(R.id.assessmentName);
            this.assessmentClickListener2 = assessmentClickListener2;

            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            try {
                mAssessmentClickListener2.onAssessmentClick2(getAdapterPosition());
            } catch (Exception e) {
                Log.println(Log.INFO,"debug", "Null click");
            }


        }


    }

    public interface assessmentClickListener2 {

        void onAssessmentClick2(int  position);

    }


    @NonNull
    @Override
    public AssessmentAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment, parent, false);


        return new MyViewHolder(itemView, mAssessmentClickListener2);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter2.MyViewHolder holder, int position) {


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
            mAssessmentClickListener2.onAssessmentClick2(holder.getAdapterPosition());


        } else {
            holder.assessmentNameText.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }

        if (checkedPosition == -1) {
            Log.println(Log.INFO,"debug", "You must select an assessment.");
        }


    }

    @Override
    public int getItemCount() {
        return allAssessmentList.size();
    }
}
