package com.example.studenttracker.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<Assessment> allAssessmentList;

    public CustomAdapter(ArrayList<Assessment> allAssessmentList) {
        this.allAssessmentList = allAssessmentList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView assessmentNameText;

        public MyViewHolder(final View view) {
            super(view);
            assessmentNameText = view.findViewById(R.id.assessmentName);

        }
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        String assessmentName = allAssessmentList.get(position).getAssessmentTitle();
        holder.assessmentNameText.setText(assessmentName);
    }

    @Override
    public int getItemCount() {
        return allAssessmentList.size();
    }
}
