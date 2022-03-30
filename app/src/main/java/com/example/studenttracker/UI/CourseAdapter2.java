package com.example.studenttracker.UI;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttracker.Models.Course;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class CourseAdapter2 extends RecyclerView.Adapter<CourseAdapter2.MyViewHolder> {

    private ArrayList<Course> allCourseList;
    private OnCourseListener2 mOnCourseListener2;

    public int checkedPosition = -1;

    public CourseAdapter2(ArrayList<Course> allCourseList, OnCourseListener2 onCourseListener2) {
        this.allCourseList = allCourseList;
        this.mOnCourseListener2 = onCourseListener2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView courseView;
        OnCourseListener2 onCourseListener2;

        public MyViewHolder(final View view, OnCourseListener2 onCourseListener){
            super(view);
            courseView = view.findViewById(R.id.courseView);
            this.onCourseListener2 = onCourseListener2;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            try {
                onCourseListener2.onCourseClick2(getAdapterPosition());
            } catch (Exception e) {
                Log.println(Log.INFO,"debug", "Null click");
            }
        }
    }

    public interface OnCourseListener2{
        void onCourseClick2(int position);

    }

    @NonNull
    @Override
    public CourseAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new MyViewHolder(courseView, mOnCourseListener2);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter2.MyViewHolder holder, int position) {
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
            mOnCourseListener2.onCourseClick2(holder.getAdapterPosition());


        } else {
            holder.courseView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
    }

    @Override
    public int getItemCount() {
        return allCourseList.size();
    }
}
