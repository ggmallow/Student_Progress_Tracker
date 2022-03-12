package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.R;

import java.text.ParseException;
import java.util.ArrayList;

public class Assessments extends AppCompatActivity implements CustomAdapter.assessmentClickListener {

    public ArrayList<Assessment> allAssessments = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView expanded;
    public Assessment selectedAssessment;
    public Integer previouslySelected = 0;
    public CustomAdapter customerAdapter;


    private Button btn; // Will use for testing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assements);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.allAssessmentsRecycler);
        allAssessments = new ArrayList<Assessment>();

        Repository repo = new Repository(getApplication());
        allAssessments.addAll(repo.getAllAssessments());

        CustomAdapter customAdapter = new CustomAdapter(allAssessments, this);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);







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
        Repository repo = new Repository(getApplication());
       // customerAdapter.notifyItemRemoved();
        repo.deleteAssessment(selectedAssessment);



    }
//Remove save button, use same form as AddAssessment
    public void assessmentDetails(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        startActivity(intent);

    }

    @Override
    public void onAssessmentClick(int position) {

        Log.println(Log.INFO,"debug", "You have picked: " + allAssessments.get(position).getAssessmentTitle());

/*
            selectedAssessment = new Assessment(
                    Integer.parseInt(String.valueOf(allAssessments.get(position).getAssessmentID())),
                    allAssessments.get(position).getAssessmentType(),
                    allAssessments.get(position).getAssessmentTitle(),
                    allAssessments.get(position).getStartDate(),
                    allAssessments.get(position).getEndDate()); */



    }
}