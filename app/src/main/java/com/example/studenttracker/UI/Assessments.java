package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Assessments extends AppCompatActivity implements AssessmentAdapter.assessmentClickListener {

    public ArrayList<Assessment> allAssessments = new ArrayList<>();

    private RecyclerView recyclerView;
    public Assessment selectedAssessment;
    public Integer previouslySelected = -1;
    public AssessmentAdapter assessmentAdapter;
    public FloatingActionButton deleteAssessment;
    public TextView assessmentsLabel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assements);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteAssessment = findViewById(R.id.deleteAssessment);
        recyclerView = findViewById(R.id.allAssessmentsRecycler);
        assessmentsLabel = findViewById(R.id.assessmentsLabel);
        allAssessments = new ArrayList<Assessment>();

        Handler handler = new Handler();
        assessmentsLabel.setText("Loading...");
        loadAssessmentData(new LoadAssessmentData() {
            @Override
            public void onComplete(List<Assessment> assessments) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        assessmentsLabel.setText("Assessments");
                        allAssessments.addAll(assessments);
                        assessmentAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


        assessmentAdapter = new AssessmentAdapter(allAssessments, this);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assessmentAdapter);

        initDelete();


    }
    interface LoadAssessmentData {
        void onComplete(List<Assessment> assessments);
    }

    private void loadAssessmentData(LoadAssessmentData callBack) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Repository repo = new Repository(getApplication());
                List<Assessment> getAllAssessments = repo.getAllAssessments();
                callBack.onComplete(getAllAssessments);
            }
        });
    }


    private void initDelete() {

        deleteAssessment.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Log.println(Log.INFO, "debug1", String.valueOf(assessmentAdapter.checkedPosition));
                if (assessmentAdapter.checkedPosition == -1) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("You must select an assessment.");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                    return;

                }
                if (selectedAssessment != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Assessments.this);
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setTitle("You are about to delete assessment: " + selectedAssessment.getAssessmentTitle());
                    alertDialogBuilder.setMessage("You can not delete an assessment, if its attached to a course..");

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Toast.makeText(Assessments.this, "You have decided against your action.", Toast.LENGTH_LONG).show();

                        }
                    });

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Repository repo = new Repository(getApplication());
                            ArrayList<Assessment> assessmentsToDelete = new ArrayList<Assessment>();
                            assessmentsToDelete.addAll(repo.getAllAssessments());


                            if (selectedAssessment.getCourseID() != null) {

                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.custom_toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_container));

                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("You can't delete this because its attached to a course.");

                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();


                            } else {

                                repo.deleteAssessment(selectedAssessment);
                                allAssessments.clear();
                                allAssessments.addAll(repo.getAllAssessments());
                                assessmentAdapter.notifyItemRemoved(previouslySelected);
                                assessmentAdapter.checkedPosition = -1;

                            }

                        }
                    });
                    alertDialogBuilder.show();

                }
            }
        });


    }

    public void addAssessment(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        startActivity(intent);
    }

    public void editAssessment(View view) {

        if (selectedAssessment == null) {

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must select an assessment.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            return;
        }

        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        intent.putExtra("assessmentID", selectedAssessment.getAssessmentID());
        startActivity(intent);
    }


//Remove save button, use same form as AddAssessment
    public void assessmentDetails(View view) {
        Intent intent = new Intent(Assessments.this,AddAssessment.class);
        int detailView = 1; // Used for UI, when navigating to detail view. This helps bc of bundle confusion.

        //Handles button click, when no assessment is selected.
        if (selectedAssessment == null) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must select an assessment.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            return;
        }

        intent.putExtra("assessmentID", selectedAssessment.getAssessmentID());
        intent.putExtra("detailView", detailView);
        startActivity(intent);

    }

    @Override
    public void onAssessmentClick(int position) {

        selectedAssessment = allAssessments.get(position);



    }


}