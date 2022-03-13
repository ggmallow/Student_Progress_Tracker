package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAssessment extends AppCompatActivity {

    public DatePickerDialog.OnDateSetListener dateSetListener;
    public DatePickerDialog datePickerDialog;
    public DatePickerDialog datePickerDialog2;

    public ArrayList<Assessment> allAssessments = new ArrayList<>();


//IDs for form
    public RadioGroup assessmentType;
    public String tempAssessmentType;
    public RadioButton objective; //Setting assessmentType
    public RadioButton performance; //Setting assessmentType
    public EditText assessmentTitle; // Setting assessmentTitle
    private TextView getStart; //Setting Start Date
    private TextView getEnd; //Setting End Date

    public Bundle moddingAssessment;
    public int modID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);
        initStartDatePicker(); // Sets up the date picker for Start Date
        initEndDatePicker(); //Sets up the date picker for End Date.

        //Setting up for Modding an Assessment
        moddingAssessment = getIntent().getExtras();
        if (moddingAssessment != null) {
            int passedPosition = moddingAssessment.getInt("moddingAssessment");
            Log.println(Log.INFO,"debug", "Data transferred: " + passedPosition);

            allAssessments = new ArrayList<Assessment>();

            assessmentType = findViewById(R.id.assessmentType);
            performance = findViewById(R.id.performance);
            objective = findViewById(R.id.objective);
            assessmentTitle = findViewById(R.id.assessmentTitle);
            getStart = findViewById(R.id.startDate);
            getEnd = findViewById(R.id.endDate);


            Repository repo = new Repository(getApplication());
            allAssessments.addAll(repo.getAllAssessments());
            allAssessments.get(passedPosition);
            Assessment modAssessment = new Assessment(
                    allAssessments.get(passedPosition).getAssessmentID(),
                    allAssessments.get(passedPosition).getAssessmentType(),
                    allAssessments.get(passedPosition).getAssessmentTitle(),
                    allAssessments.get(passedPosition).getStartDate(),
                    allAssessments.get(passedPosition).getEndDate());
                    modID = modAssessment.getAssessmentID();

        if (modAssessment.getAssessmentType().equals("Performance")) {
            performance.setChecked(true);
        } else {
            objective.setChecked(true);
        }
        assessmentTitle.setText(modAssessment.getAssessmentTitle());
        getStart.setText(modAssessment.getStartDate());
        getEnd.setText(modAssessment.getEndDate());

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

    public void saveAssessment(View view) throws ParseException {
        assessmentType = findViewById(R.id.assessmentType);
        performance = findViewById(R.id.performance);
        objective = findViewById(R.id.objective);
        assessmentTitle = findViewById(R.id.assessmentTitle);
        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);


        if (assessmentType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "You must select the Assessment Type", Toast.LENGTH_LONG).show();
            return;
        }

        if (performance.isChecked()) {
            tempAssessmentType = "Performance";
        } else if (objective.isChecked()) {
            tempAssessmentType = "Objective";
        }

        if (assessmentTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "You haven't entered a title", Toast.LENGTH_LONG).show();
            return;
        }
        if (assessmentTitle.getText().toString().length() > 25) {
            Toast.makeText(this, "Please choose a title 25 or less characters.", Toast.LENGTH_LONG).show();
            return;
        }
        if (getStart.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must pick a Start Date", Toast.LENGTH_LONG).show();
            return;
        }
        /*
//This is not complete
        if (getStart.getText().toString().length() > 0 && getEnd.getText().toString().length() > 0) {
            Toast.makeText(this, "This will contain logic for date checking.", Toast.LENGTH_LONG).show();
            return;
        }
        */

        if (getEnd.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must pick a End Date", Toast.LENGTH_LONG).show();

        } else if (moddingAssessment != null) {
            Log.println(Log.INFO,"debug", "Mod assessment logic here");
            Repository repo = new Repository(getApplication());
            Assessment modAssessment = new Assessment(modID,tempAssessmentType, assessmentTitle.getText().toString(), getStart.getText().toString(), getEnd.getText().toString());
            repo.updateAssessment(modAssessment);
            Toast.makeText(this, "Modification Complete, Check Database.", Toast.LENGTH_LONG).show();
        }else {

            //Use 0 to have ID auto generated. https://developer.android.com/reference/androidx/room/PrimaryKey#autoGenerate()
            Repository repo = new Repository(getApplication());
            Assessment newAssessment = new Assessment(0,tempAssessmentType, assessmentTitle.getText().toString(), getStart.getText().toString(), getEnd.getText().toString());
            repo.insertAssessment(newAssessment);
            Toast.makeText(this, "Assessment Saved, Check Database.", Toast.LENGTH_LONG).show();
        }

    }
}