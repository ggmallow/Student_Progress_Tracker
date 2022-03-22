package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;
import com.example.studenttracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTerm extends AppCompatActivity implements CourseAdapter.OnCourseListener{

    public TextView termName;
    public TextView getStart;
    public TextView getEnd;
    public Button attachCourse;
    public Button detachCourse;
    public Button saveTerm;
    public TextView detailsInfo;

    public DatePickerDialog.OnDateSetListener dateSetListener;
    public DatePickerDialog datePickerDialog;
    public DatePickerDialog datePickerDialog2;

    public ArrayList<Course> allCourses = new ArrayList<>();
    public RecyclerView allCoursesRecycler;

    public Course selectedCourse; // Used to pick courses in Recycler View
    public Integer previouslySelected = -1; //Detects clicked location in Recycler View.
    public CourseAdapter courseAdapter; // Borrowed adapter from Courses activity, to display data properly.

    public Bundle moddingTerm; // Transferred Term position.
    public Bundle termDetails; // Transferred Term position.

    public int modID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);


        termName = findViewById(R.id.termName);
        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);
        attachCourse = findViewById(R.id.attachCourse);
        detachCourse = findViewById(R.id.detachCourse);
        saveTerm = findViewById(R.id.saveTerm);
        detailsInfo = findViewById(R.id.detailsInfo);
        initStartDatePicker(); // Sets up the date picker for Start Date
        initEndDatePicker(); //Sets up the date picker for End Date.

        detailsInfo.setVisibility(View.GONE);

        allCourses = new ArrayList<Course>(); //Initiating new ArrayList
        Repository repo = new Repository(getApplication()); //Creating new Repository to get Courses.
        allCourses.addAll(repo.getAllCourses()); // Actually adding all courses from the allCourses database.

        courseAdapter = new CourseAdapter(allCourses, this); // Change allCourses to array of courses attached.
        allCoursesRecycler = findViewById(R.id.attachedCourses);
        RecyclerView.LayoutManager courseLayout = new LinearLayoutManager(getApplicationContext());
        allCoursesRecycler.setLayoutManager(courseLayout);
        allCoursesRecycler.setItemAnimator(new DefaultItemAnimator());
        allCoursesRecycler.setAdapter(courseAdapter);

        modTermInit();
        termDetailsInit();
    }

    private void termDetailsInit() {
        try {
            termDetails = getIntent().getExtras();
            if (termDetails.getInt("detailView") == 1) {
                int passedPosition = termDetails.getInt("termDetails");
                //Disabling all fields to prevent editing
                termName.setEnabled(false);
                getStart.setEnabled(false);
                getEnd.setEnabled(false);
                allCoursesRecycler.setEnabled(false);
                attachCourse.setEnabled(false);
                detachCourse.setEnabled(false);
                saveTerm.setVisibility(View.GONE);
                detailsInfo.setVisibility(View.VISIBLE);



                ArrayList<Term> allTerms = new ArrayList<Term>();

                Repository repo = new Repository(getApplication());
                allTerms.addAll(repo.getAllTerms());

                Term modTerm = new Term(
                        allTerms.get(passedPosition).getTermID(),
                        allTerms.get(passedPosition).getTitle(),
                        allTerms.get(passedPosition).getStartDate(),
                        allTerms.get(passedPosition).getEndDate());

                termName.setText(modTerm.getTitle());
                getStart.setText(modTerm.getStartDate());
                getEnd.setText(modTerm.getEndDate());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.println(Log.INFO,"debug", "Null pointer has occurred.");
        }
    }

    private void modTermInit() {
        moddingTerm = getIntent().getExtras();
        if (moddingTerm != null) {
            int passedPosition = moddingTerm.getInt("moddingTerm");
            ArrayList<Term> allTerms = new ArrayList<Term>();

            Repository repo = new Repository(getApplication());
            allTerms.addAll(repo.getAllTerms());

            Term modTerm = new Term(
                    allTerms.get(passedPosition).getTermID(),
                    allTerms.get(passedPosition).getTitle(),
                    allTerms.get(passedPosition).getStartDate(),
                    allTerms.get(passedPosition).getEndDate());
            modID = allTerms.get(passedPosition).getTermID();

            termName.setText(modTerm.getTitle());
            getStart.setText(modTerm.getStartDate());
            getEnd.setText(modTerm.getEndDate());

        }
    }


    // This is setting up the Start Date Picker.
    public void openStartDatePicker(View view) {
        datePickerDialog.show();
    }

    public void initStartDatePicker() {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month = month + 1;

                String makeDateString = makeDateString(day, month, year);
                getStart.setText(makeDateString);
            }
        };
        Calendar myCal = Calendar.getInstance();
        int year = myCal.get(Calendar.YEAR);
        int month = myCal.get(Calendar.MONTH);
        int day = myCal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
    }


    // This is setting up the End Date Picker.
    public void openEndDatePicker(View view) {
        datePickerDialog2.show();
    }

    public void initEndDatePicker() {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month = month + 1;

                String makeDateString = makeDateString(day, month, year);
                getEnd.setText(makeDateString);
            }
        };
        Calendar myCal = Calendar.getInstance();
        int year = myCal.get(Calendar.YEAR);
        int month = myCal.get(Calendar.MONTH);
        int day = myCal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog2 = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
    }


    // Reused by both endDatePicker and startDatePicker
    private String makeDateString(int day,int month,int year) {
    return getMonthFormat(month) + " " + day + " " + year;
    }


    private String getMonthFormat(int month) {
        if (month == 1) {
            return "JAN";
        }
        if (month == 2) {
            return "FEB";
        }
        if (month == 3) {
            return "MAR";
        }
        if (month == 4) {
            return "APR";
        }
        if (month == 5) {
            return "MAY";
        }
        if (month == 6) {
            return "JUN";
        }
        if (month == 7) {
            return "JUL";
        }
        if (month == 8) {
            return "AUG";
        }
        if (month == 9) {
            return "SEP";
        }
        if (month == 10) {
            return "OCT";
        }
        if (month == 11) {
            return "NOV";
        }
        if (month == 12) {
            return "DEC";
        }
        return "JAN"; //This statement should never be reached.

    }

    public void saveTerm(View view) throws ParseException {

        if (termName.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't entered a Term Name", Toast.LENGTH_LONG).show();
            return;
        }

        if (getStart.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't set a Start Date", Toast.LENGTH_LONG).show();
            return;
        }

        if (getEnd.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't set a End Date", Toast.LENGTH_LONG).show();
            return;
        }
        // Setting a date formatter to convert strings to actual Dates. https://www.baeldung.com/java-string-to-date
        SimpleDateFormat test = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
        Date startDate = test.parse(getStart.getText().toString()); // Converting String, getStart to startDate as a Date Object.
        Date endDate = test.parse(getEnd.getText().toString()); // Converting String, getEnd to endDate as a Date Object.

        //Making sure Start Date is before the End Date
        if (endDate.before(startDate)) {
            Toast.makeText(this, "Term End Date must be after Start Date.", Toast.LENGTH_LONG).show();
            return;
        }
        //Not allowing for Start Date to be equal to End Date.
        if (startDate.equals(endDate)) {
            Toast.makeText(this, "Term Start Date can not equal End Date.", Toast.LENGTH_LONG).show();
        } else if (moddingTerm != null) {
            Log.println(Log.INFO,"debug", "Mod Term logic here");
            Repository repo = new Repository(getApplication());
            Term modTerm = new Term(modID, termName.getText().toString(), getStart.getText().toString(), getEnd.getText().toString());
            repo.updateTerm(modTerm);
            Toast.makeText(this, "Modification Complete, Check Database.", Toast.LENGTH_LONG).show();
        } else {
            Repository repo = new Repository(getApplication());
            Term newTerm = new Term(0,
                    termName.getText().toString(),
                    getStart.getText().toString(),
                    getEnd.getText().toString());
            repo.insertTerm(newTerm);
            Toast.makeText(this, "Term Saved, Check Database.", Toast.LENGTH_LONG).show();

        }




    }

    @Override
    public void onCourseClick(int position) {


    }
}
