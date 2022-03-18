package com.example.studenttracker.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private ArrayList<Course> allCourseList;
    public int checkedPosition = -1;

    public CourseAdapter(ArrayList<Course> allCourseList) {
        this.allCourseList = allCourseList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView courseView;

        public MyViewHolder(final View view){
            super(view);
            courseView = view.findViewById(R.id.courseView);

        }
    }


    @NonNull
    @Override
    public CourseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new MyViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.MyViewHolder holder, int position) {
        String courseName = allCourseList.get(position).getTitle();
        holder.courseView.setText(courseName);
    }

    @Override
    public int getItemCount() {
        return allCourseList.size();
    }
}
