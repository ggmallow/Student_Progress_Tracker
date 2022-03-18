package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    public RecyclerView allCoursesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        allCourses = new ArrayList<Course>(); //Initiating new ArrayList
        Repository repo = new Repository(getApplication()); //Creatting new Repository to get Courses.
        allCourses.addAll(repo.getAllCourses()); // Actually adding all courses from the allCourses database.

        CourseAdapter courseAdapter = new CourseAdapter(allCourses);
        allCoursesRecycler = findViewById(R.id.allCoursesRecycler);
        RecyclerView.LayoutManager courseLayout = new LinearLayoutManager(getApplicationContext());
        allCoursesRecycler.setLayoutManager(courseLayout);
        allCoursesRecycler.setItemAnimator(new DefaultItemAnimator());
        allCoursesRecycler.setAdapter(courseAdapter);

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