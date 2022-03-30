package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.service.controls.actions.FloatAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Courses extends AppCompatActivity implements CourseAdapter.OnCourseListener {
    public ArrayList<Course> allCourses = new ArrayList<>();
    public RecyclerView allCoursesRecycler;

    public Course selectedCourse;
    public CourseAdapter courseAdapter;

    public FloatingActionButton deleteCourse;

    public TextView courseLabel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteCourse = findViewById(R.id.deleteCourse);
        courseLabel = findViewById(R.id.courseLabel);

        allCourses = new ArrayList<Course>(); //Initiating new ArrayList


        courseAdapter = new CourseAdapter(allCourses, Courses.this);

        allCoursesRecycler = findViewById(R.id.allCoursesRecycler);
        RecyclerView.LayoutManager courseLayout = new LinearLayoutManager(getApplicationContext());
        allCoursesRecycler.setLayoutManager(courseLayout);
        allCoursesRecycler.setItemAnimator(new DefaultItemAnimator());
        allCoursesRecycler.setAdapter(courseAdapter);

        initDeleteCourse(); //Sets up the delete Course button.


        Handler handler = new Handler();
        courseLabel.setText("Loading...");
        loadCourseData(  new LoadCourseDataCallback () {
            @Override
            public void onComplete(List<Course> courses) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        courseLabel.setText("Courses");
                        allCourses.addAll(courses); // Actually adding all courses from the allCourses database.
                        courseAdapter.notifyDataSetChanged();

                    }
                });

            }
        });



    }

    interface LoadCourseDataCallback {
        void onComplete(List<Course> courses);
    }


    private void loadCourseData(LoadCourseDataCallback  callBack) {


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Repository repo = new Repository(getApplication()); //Creating new Repository to get Courses.
                List<Course> allCourseList = repo.getAllCourses();
                callBack.onComplete(allCourseList);


            }
        });


    }

    private void initDeleteCourse() {

        deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseAdapter.checkedPosition == -1) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("You must select a course.");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                    return;
                }

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Courses.this);
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setTitle("You are about to delete: " + selectedCourse.getTitle());
                    alertDialogBuilder.setMessage("By clicking OK, all Assessments attached to this course will also be deleted.");

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Toast.makeText(Courses.this, "You have decided against your action.", Toast.LENGTH_LONG).show();

                        }
                    });

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Repository repo = new Repository(getApplication());
                            ArrayList<Assessment> assessmentsToDelete = new ArrayList<Assessment>();
                            assessmentsToDelete.addAll(repo.getAllAssessments());


                            for (Assessment toDelete : assessmentsToDelete) {
                                if (toDelete.getCourseID() == selectedCourse.getCourseID()) {
                                    repo.deleteAssessment(toDelete);
                                }
                            }
                            int selectedCourseIndex = allCourses.indexOf(selectedCourse);

                            repo.deleteCourse(selectedCourse);

                            allCourses.remove(selectedCourse);
                            courseAdapter.notifyItemRemoved(selectedCourseIndex);
                            courseAdapter.checkedPosition = -1;

                        }
                    });
                    alertDialogBuilder.show();

            }
        });

    }

    public void addCourse(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        startActivity(intent);
    }

    public void editCourse(View view) {
        if (selectedCourse == null) {

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must select a course.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            return;
        }
        Intent intent = new Intent(Courses.this,AddCourse.class);
        intent.putExtra("courseID", selectedCourse.getCourseID());
        startActivity(intent);
    }


    //Remove save button, use same form as AddCourse
    public void courseDetails(View view) {
        Intent intent = new Intent(Courses.this,AddCourse.class);
        int detailView = 1; // Used for UI, when navigating to detail view. This helps bc of bundle confusion.

        //Handles button click, when no assessment is selected.
        if (selectedCourse == null) {

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must select a course.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            return;
        }

        intent.putExtra("courseID", selectedCourse.getCourseID());
        intent.putExtra("detailView", detailView);
        startActivity(intent);
    }

    @Override
    public void onCourseClick(int position) {
        selectedCourse = allCourses.get(position);

    }
}