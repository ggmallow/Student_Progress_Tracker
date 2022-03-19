package com.example.studenttracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Instructor;
import com.example.studenttracker.R;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public DatePickerDialog.OnDateSetListener dateSetListener;
    public DatePickerDialog datePickerDialog;
    public DatePickerDialog datePickerDialog2;

    private TextView courseTitle; //Setting Course Title.
    private TextView getStart; //Setting Start Date.
    private TextView getEnd; //Setting End Date.
    public TextView detailsInfo;

    public Spinner statusSpinner; //Setting up Spinner for Status.
    public ArrayAdapter statusAdapter;

    public Spinner instructorSpinner; //Setting up Spinner for Instructors.
    public ArrayAdapter instructorAdapter;
    public ArrayList<Instructor> instructorList = new ArrayList<>();

    public TextView instructorPhone; //Setting up Instructor Phone TextView.
    public TextView instructorEmail; //Setting up Instructor Email TextView.

    public Button saveCourse;

    //Used for modding course
    public Bundle moddingCourse;
    public ArrayList<Course> allCourses;
    public int modID;

    //Used for detailed View
    public Bundle courseDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);
        detailsInfo = findViewById(R.id.detailsInfo);
        detailsInfo.setVisibility(View.GONE);

        initStartDatePicker(); // Sets up the date picker for Start Date
        initEndDatePicker(); //Sets up the date picker for End Date.

        //Setting up Status Spinner.
        statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_dropdown_item);
        statusSpinner = findViewById(R.id.status);
        statusSpinner.setAdapter(statusAdapter);


        //Adding instructors
        Instructor testInstructor = new Instructor("John", 1234561234, "test@wgu.edu");
        instructorList.add(testInstructor);
        Instructor testInstructor2 = new Instructor("Matt", 189123456, "test2@wgu.edu");
        instructorList.add(testInstructor2);
        initInstructorPicker(); //Loads menu with instructors.

        modCourseInit();  //Setting up for modding Course.
        courseDetailsInit(); //Setting up if navigating form Course Details button.




    }

    private void courseDetailsInit() {
        try {
            courseDetails = getIntent().getExtras();
            if (courseDetails.getInt("detailView") == 1) {

            allCourses = new ArrayList<Course>();

            courseTitle = findViewById(R.id.courseTitle); //Setting Course Title.
            getStart = findViewById(R.id.startDate); //Setting Start Date.
            getEnd = findViewById(R.id.endDate); //Setting End Date.

            statusSpinner = findViewById(R.id.status); //Setting up Spinner for Status.

            instructorSpinner = findViewById(R.id.instructor); //Setting up Spinner for Instructors.

            instructorPhone = findViewById(R.id.instructorPhone); //Setting up Instructor Phone TextView.
            instructorEmail = findViewById(R.id.instructorEmail); //Setting up Instructor Email TextView.
            saveCourse = findViewById(R.id.saveCourse);

            //Disabling to prevent editing.
            courseTitle.setEnabled(false);
            getStart.setEnabled(false);
            getEnd.setEnabled(false);
            statusSpinner.setEnabled(false);
            instructorSpinner.setEnabled(false);
            instructorPhone.setEnabled(false);
            instructorEmail.setEnabled(false);
            saveCourse.setVisibility(View.GONE);
            detailsInfo.setVisibility(View.VISIBLE);




            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to set up fields if coming from the Course edit button.
    private void modCourseInit() {

        moddingCourse = getIntent().getExtras();
        if (moddingCourse != null) {
            courseTitle = findViewById(R.id.courseTitle); //Setting Course Title.
            getStart = findViewById(R.id.startDate); //Setting Start Date.
            getEnd = findViewById(R.id.endDate); //Setting End Date.

            statusSpinner = findViewById(R.id.status); //Setting up Spinner for Status.

            instructorSpinner = findViewById(R.id.instructor); //Setting up Spinner for Instructors.

            instructorPhone = findViewById(R.id.instructorPhone); //Setting up Instructor Phone TextView.
            instructorEmail = findViewById(R.id.instructorEmail); //Setting up Instructor Email TextView.

            allCourses = new ArrayList<Course>();

            int passedPosition = moddingCourse.getInt("moddingCourse");

            Repository repo = new Repository(getApplication());
            allCourses.addAll(repo.getAllCourses());
            Course modifiedCourse = new Course(
                    allCourses.get(passedPosition).getCourseID(),
                    allCourses.get(passedPosition).getTitle(),
                    allCourses.get(passedPosition).getStartDate(),
                    allCourses.get(passedPosition).getEndDate(),
                    allCourses.get(passedPosition).getStatus());

            courseTitle.setText(modifiedCourse.getTitle());
            getStart.setText(modifiedCourse.getStartDate());
            getEnd.setText(modifiedCourse.getEndDate());
            modID = modifiedCourse.getCourseID();


            statusSpinner.setSelection(statusAdapter.getPosition(allCourses.get(passedPosition).getStatus()));

        }
    }

    private void initInstructorPicker() {
        //Setting up instructor box.
        instructorAdapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_spinner_item, instructorList);

        instructorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructorSpinner = findViewById(R.id.instructor);
        instructorSpinner.setAdapter(instructorAdapter);

        instructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Instructor selectedInstructor = (Instructor) adapterView.getItemAtPosition(i);
                Log.println(Log.INFO,"debug", "Data transferred: " + selectedInstructor.getName());
                instructorPhone.setText(String.valueOf(selectedInstructor.getPhone()));
                instructorEmail.setText(selectedInstructor.getEmail());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        //Getting rest of instructor boxes
        instructorPhone = findViewById(R.id.instructorPhone);
        instructorEmail = findViewById(R.id.instructorEmail);
        //Disabling both fields as they can not be edited.
        instructorPhone.setEnabled(false);
        instructorEmail.setEnabled(false);


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

    // This is setting up the Start Date Picker.
    public void openStartDatePicker(View view) {
        datePickerDialog.show();
    }

    // This is setting up the End Date Picker.
    public void openEndDatePicker(View view) {
        datePickerDialog2.show();
    }

    // Reused by both endDatePicker and startDatePicker
    private String makeDateString(int day,int month,int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    //Formats month as String name.
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



    //Used for OnItemSelected Listener.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//Work needed but parts now enter the database. Need to check Dates for errors still.
    public void saveCourse(View view) {
        courseTitle = findViewById(R.id.courseTitle); //Setting Course Title.
        getStart = findViewById(R.id.startDate); //Setting Start Date.
        getEnd = findViewById(R.id.endDate); //Setting End Date.

        statusSpinner = findViewById(R.id.status); //Setting up Spinner for Status.

        instructorSpinner = findViewById(R.id.instructor); //Setting up Spinner for Instructors.

        instructorPhone = findViewById(R.id.instructorPhone); //Setting up Instructor Phone TextView.
        instructorEmail = findViewById(R.id.instructorEmail); //Setting up Instructor Email TextView.

        if (courseTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't entered a title", Toast.LENGTH_LONG).show();
            return;
        }
        if (getStart.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't entered a Start Date", Toast.LENGTH_LONG).show();
            return;
        }
        if (getEnd.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't entered a End Date", Toast.LENGTH_LONG).show();
            return;
        }
        if (statusSpinner.getSelectedItem().equals("Select a Status")) {
                Toast.makeText(this, "You must select a valid status", Toast.LENGTH_LONG).show();


        } else if (moddingCourse != null) {
            Repository repo = new Repository(getApplication());
            Course modCourse = new Course(modID,
                    courseTitle.getText().toString(),
                    getStart.getText().toString(),
                    getEnd.getText().toString(),
                    statusSpinner.getSelectedItem().toString());
            repo.updateCourse(modCourse);
            Toast.makeText(this, "Modification Complete, Check Database.", Toast.LENGTH_LONG).show();
        }else {

            Repository repo = new Repository(getApplication());
            Course newCourse = new Course(0,
                    courseTitle.getText().toString(),
                    getStart.getText().toString(),
                    getEnd.getText().toString(),
                    statusSpinner.getSelectedItem().toString());
            repo.insertCourse(newCourse);
            Toast.makeText(this, "Course Saved, Check Database.", Toast.LENGTH_LONG).show();
        }


    }


}
