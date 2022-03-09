package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class Assessments extends AppCompatActivity {

    public ArrayList<Assessment> allAssessments = new ArrayList<>();
    public Integer testSelected = null;

    private RecyclerView recyclerView;


    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assements);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.allAssessmentsRecycler);
        allAssessments = new ArrayList<Assessment>();

        Repository repo = new Repository(getApplication());
        allAssessments.addAll(repo.getAllAssessments());

        CustomAdapter customAdapter = new CustomAdapter(allAssessments);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);




/*
        recyclerView = findViewById(R.id.allAssessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(Assessments.this));
        Repository repo = new Repository(getApplication());
        testArray.addAll(repo.getAllAssessments());
        adapter = new SingleAdapter(Assessments.this, testArray, -1);
        recyclerView.setAdapter(adapter); */






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