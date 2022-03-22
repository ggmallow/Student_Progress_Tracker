package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class Courses extends AppCompatActivity implements CourseAdapter.OnCourseListener {
    public ArrayList<Course> allCourses = new ArrayList<>();
    public RecyclerView allCoursesRecycler;

    public Course selectedCourse;
    public Integer previouslySelected = -1;
    public CourseAdapter courseAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        allCourses = new ArrayList<Course>(); //Initiating new ArrayList
        Repository repo = new Repository(getApplication()); //Creating new Repository to get Courses.
        allCourses.addAll(repo.getAllCourses()); // Actually adding all courses from the allCourses database.

        courseAdapter = new CourseAdapter(allCourses, this);
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
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select a course.", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(Courses.this,AddCourse.class);
        intent.putExtra("moddingCourse", previouslySelected);
        startActivity(intent);
    }

    public void deleteCourse(View view) {
        //Handles button click, when no assessment is selected.
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select a course.", Toast.LENGTH_LONG).show();
            return;
        }

        Repository repo = new Repository(getApplication());
        repo.deleteCourse(selectedCourse);
        allCourses.clear();
        allCourses.addAll(repo.getAllCourses());
        courseAdapter.notifyItemRemoved(previouslySelected);
    }

    //Remove save button, use same form as AddCourse
    public void courseDetails(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        int detailView = 1; // Used for UI, when navigating to detail view. This helps bc of bundle confusion.

        //Handles button click, when no assessment is selected.
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select a course.", Toast.LENGTH_LONG).show();
            return;
        }

        intent.putExtra("courseDetails", previouslySelected);
        intent.putExtra("detailView", detailView);
        startActivity(intent);
    }

    @Override
    public void onCourseClick(int position) {
        Log.println(Log.INFO,"debug", "You have picked: " + allCourses.get(position).getTitle());
        previouslySelected = position;

        //Creates an course based off selection. Used for delete method.
        selectedCourse = new Course (
                allCourses.get(position).getCourseID(),
                allCourses.get(position).getTitle(),
                allCourses.get(position).getStartDate(),
                allCourses.get(position).getEndDate(),
                allCourses.get(position).getStatus(),
                allCourses.get(position).getInstructor(),
                allCourses.get(position).getCourseNotes());

    }
}