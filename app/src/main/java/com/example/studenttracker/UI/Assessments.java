package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.studenttracker.R;

public class Assessments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assements);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addAssessment(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        startActivity(intent);
    }

    public void editAssessment(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        startActivity(intent);
    }

    public void deleteAssessment(View view) {
    }
//Remove save button, use same form as AddAssessment
    public void assessmentDetails(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        startActivity(intent);
    }
}