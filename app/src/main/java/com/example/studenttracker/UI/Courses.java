package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.studenttracker.R;

public class Courses extends AppCompatActivity {

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
    }

    //Remove save button, use same form as AddCourse
    public void courseDetails(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        startActivity(intent);
    }
}