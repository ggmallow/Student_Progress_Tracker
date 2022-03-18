package com.example.studenttracker.UI;

import android.graphics.Color;
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
    private OnCourseListener mOnCourseListener;

    public int checkedPosition = -1;

    public CourseAdapter(ArrayList<Course> allCourseList, OnCourseListener onCourseListener) {
        this.allCourseList = allCourseList;
        this.mOnCourseListener = onCourseListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView courseView;
        OnCourseListener onCourseListener;

        public MyViewHolder(final View view, OnCourseListener onCourseListener){
            super(view);
            courseView = view.findViewById(R.id.courseView);
            this.onCourseListener = onCourseListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onCourseListener.onCourseClick(getAdapterPosition());
        }
    }

    public interface OnCourseListener{
        void onCourseClick(int position);

    }

    @NonNull
    @Override
    public CourseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new MyViewHolder(courseView, mOnCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.MyViewHolder holder, int position) {
        String courseName = allCourseList.get(position).getTitle();
        holder.courseView.setText(courseName);

        holder.courseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();

            }
        } );

        if (checkedPosition == position) {
            holder.courseView.setBackgroundColor(Color.parseColor("#FF018786"));
            mOnCourseListener.onCourseClick(holder.getAdapterPosition());


        } else {
            holder.courseView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
    }

    @Override
    public int getItemCount() {
        return allCourseList.size();
    }
}
