package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class Courses extends AppCompatActivity {
    public ArrayList<Course> allCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public void addCourse(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        startActivity(intent);
    }


    public void editCourse(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        startActivity(intent);
    }

    public void deleteCourse(View view) {
        Repository repo = new Repository(getApplication());
        Course test = new Course (1, "t", "MAR 17 2022", "MAR 17 2022", "In Progress");
        repo.deleteCourse(test);
        allCourses.clear();
        allCourses.addAll(repo.getAllCourses());
      //  courseAdapter.notifyItemRemoved(previouslySelected); This will be enabled after UI is finished.
    }

    //Remove save button, use same form as AddCourse
    public void courseDetails(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        startActivity(intent);
    }
}