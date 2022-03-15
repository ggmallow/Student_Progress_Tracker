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
import com.example.studenttracker.R;

import java.util.ArrayList;

public class Assessments extends AppCompatActivity implements AssessmentAdapter.assessmentClickListener {

    public ArrayList<Assessment> allAssessments = new ArrayList<>();

    private RecyclerView recyclerView;
    public Assessment selectedAssessment;
    public Integer previouslySelected = -1;
    public AssessmentAdapter assessmentAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assements);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.allAssessmentsRecycler);
        allAssessments = new ArrayList<Assessment>();

        Repository repo = new Repository(getApplication());
        allAssessments.addAll(repo.getAllAssessments());

        assessmentAdapter = new AssessmentAdapter(allAssessments, this);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assessmentAdapter);


    }

    public void addAssessment(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        startActivity(intent);
    }

    public void editAssessment(View view) {

        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select an assessment.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        intent.putExtra("moddingAssessment", previouslySelected);
        startActivity(intent);
    }


    public void deleteAssessment(View view) {
        //Handles button click, when no assessment is selected.
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select an assessment.", Toast.LENGTH_LONG).show();
            return;
        }

        Repository repo = new Repository(getApplication());
        repo.deleteAssessment(selectedAssessment);
        allAssessments.clear();
        allAssessments.addAll(repo.getAllAssessments());
        assessmentAdapter.notifyItemRemoved(previouslySelected);




    }
//Remove save button, use same form as AddAssessment
    public void assessmentDetails(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        int detailView = 1; // Used for UI, when navigating to detail view. This helps bc of bundle confusion.

        //Handles button click, when no assessment is selected.
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select an assessment.", Toast.LENGTH_LONG).show();
            return;
        }

        intent.putExtra("assessmentDetails", previouslySelected);
        intent.putExtra("detailView", detailView);
        startActivity(intent);

    }

    @Override
    public void onAssessmentClick(int position) {

        Log.println(Log.INFO,"debug", "You have picked: " + allAssessments.get(position).getAssessmentTitle());
        previouslySelected = position;

        //Creates an assessment based off selection. Used for delete method.
            selectedAssessment = new Assessment(
                    Integer.parseInt(String.valueOf(allAssessments.get(position).getAssessmentID())),
                    allAssessments.get(position).getAssessmentType(),
                    allAssessments.get(position).getAssessmentTitle(),
                    allAssessments.get(position).getStartDate(),
                    allAssessments.get(position).getEndDate());

    }


}