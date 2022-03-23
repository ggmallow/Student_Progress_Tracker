package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.time.format.DateTimeFormatter;
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
    public Button saveAssessment;
    public TextView detailsInfo;

    public Bundle moddingAssessment;
    public Bundle assessmentDetails;

    public int modID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);
        detailsInfo = findViewById(R.id.detailsInfo);
        detailsInfo.setVisibility(View.GONE);
        initStartDatePicker(); // Sets up the date picker for Start Date
        initEndDatePicker(); //Sets up the date picker for End Date.


        modAssessmentInit(); //Setting up for Modding an Assessment.

        assessmentDetailsInit(); //Setting up for Assessment Detail View.





    }
    //Method that provides all data to the form, if navigating from details button. It also disables the fields and hides the save button.
    private void assessmentDetailsInit() {

        try {
            assessmentDetails = getIntent().getExtras();
            if (assessmentDetails.getInt("detailView") == 1) {
                int passedPosition = assessmentDetails.getInt("assessmentDetails");

                allAssessments = new ArrayList<Assessment>();

                assessmentType = findViewById(R.id.assessmentType);
                performance = findViewById(R.id.performance);
                objective = findViewById(R.id.objective);
                assessmentTitle = findViewById(R.id.assessmentTitle);
                getStart = findViewById(R.id.startDate);
                getEnd = findViewById(R.id.endDate);
                saveAssessment = findViewById(R.id.saveAssessment);



                //Disabling fields, so they can not be edited.
                assessmentType.setEnabled(false);
                performance.setEnabled(false);
                objective.setEnabled(false);
                assessmentTitle.setEnabled(false);
                getStart.setEnabled(false);
                getEnd.setEnabled(false);
                saveAssessment.setVisibility(View.GONE);
                detailsInfo.setVisibility(View.VISIBLE);




                Repository repo = new Repository(getApplication());
                allAssessments.addAll(repo.getAllAssessments());
                allAssessments.get(passedPosition);
                Assessment assessmentDetails = new Assessment(
                        allAssessments.get(passedPosition).getAssessmentID(),
                        allAssessments.get(passedPosition).getAssessmentType(),
                        allAssessments.get(passedPosition).getAssessmentTitle(),
                        allAssessments.get(passedPosition).getStartDate(),
                        allAssessments.get(passedPosition).getEndDate(), null);

                //Populating form data
                if (assessmentDetails.getAssessmentType().equals("Performance")) {
                    performance.setChecked(true);
                } else {
                    objective.setChecked(true);
                }
                assessmentTitle.setText(assessmentDetails.getAssessmentTitle());
                getStart.setText(assessmentDetails.getStartDate());
                getEnd.setText(assessmentDetails.getEndDate());
            }
        } catch (Exception e) {
          //  e.printStackTrace(); Used during debugging, handling problem below.
            Log.println(Log.INFO,"debug", "Null pointer has occurred.");
        }


    }

    //Method that provides all data to the form, if navigating from edit button.
    private void modAssessmentInit() {
        moddingAssessment = getIntent().getExtras();
        if (moddingAssessment != null) {
            int passedPosition = moddingAssessment.getInt("moddingAssessment");
            Log.println(Log.INFO,"debug", "Data transferred: " + passedPosition);
            Log.println(Log.INFO,"debug", "moddingAssessment: " + moddingAssessment);


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
                    allAssessments.get(passedPosition).getEndDate(), null);
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

    public void saveAssessment(View view) throws ParseException {
        assessmentType = findViewById(R.id.assessmentType);
        performance = findViewById(R.id.performance);
        objective = findViewById(R.id.objective);
        assessmentTitle = findViewById(R.id.assessmentTitle);
        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);

        try {
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

            if (getEnd.getText().toString().isEmpty()) {
                Toast.makeText(this, "You must pick a End Date", Toast.LENGTH_LONG).show();
                return;
            }
            // Setting a date formatter to convert strings to actual Dates. https://www.baeldung.com/java-string-to-date
            SimpleDateFormat date = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
            Date startDate = date.parse(getStart.getText().toString()); // Converting String, getStart to startDate as a Date Object.
            Date endDate = date.parse(getEnd.getText().toString()); // Converting String, getEnd to endDate as a Date Object.

            //Making sure Start Date is before the End Date
            if (endDate.before(startDate)) {
                Toast.makeText(this, "Assessment End Date must be after Start Date.", Toast.LENGTH_LONG).show();
                return;
            }
            //Not allowing for Start Date to be equal to End Date.
            if (startDate.equals(endDate)) {
                Toast.makeText(this, "Assessment Start Date can not equal End Date.", Toast.LENGTH_LONG).show();
            }

            else if (moddingAssessment != null) {
                Log.println(Log.INFO,"debug", "Mod assessment logic here");
                Repository repo = new Repository(getApplication());
                Assessment modAssessment = new Assessment(modID,tempAssessmentType, assessmentTitle.getText().toString(), getStart.getText().toString(), getEnd.getText().toString(), null);
                repo.updateAssessment(modAssessment);

                Long alertStartTime = startDate.getTime();
                Long alertEndTime = endDate.getTime();

                Intent notificationStartIntent = new Intent(AddAssessment.this, MyReceiver.class);
                Intent notificationEndIntent = new Intent(AddAssessment.this, MyReceiver.class);

                notificationStartIntent.putExtra("key", "Assessment starts today: " + assessmentTitle.getText());
                notificationEndIntent.putExtra("key", assessmentTitle.getText() + " Assessment Ends today.");

                //Pending intents
                PendingIntent startTime = PendingIntent.getBroadcast(AddAssessment.this, MainActivity.alertNum++, notificationStartIntent, 0);
                PendingIntent endTime = PendingIntent.getBroadcast(AddAssessment.this, MainActivity.alertNum++, notificationEndIntent, 0);

                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManagerStart.set(AlarmManager.RTC_WAKEUP,alertStartTime, startTime);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP,alertEndTime, endTime);

                Toast.makeText(this, "Modification Complete, Check Database.", Toast.LENGTH_LONG).show();
            }else {

                //Use 0 to have ID auto generated. https://developer.android.com/reference/androidx/room/PrimaryKey#autoGenerate()
                Repository repo = new Repository(getApplication());
                Assessment newAssessment = new Assessment(
                        0,
                        tempAssessmentType,
                        assessmentTitle.getText().toString(),
                        getStart.getText().toString(),
                        getEnd.getText().toString(),
                        null);
                repo.insertAssessment(newAssessment);

                Long alertStartTime = startDate.getTime();
                Long alertEndTime = endDate.getTime();

                Intent notificationStartIntent = new Intent(AddAssessment.this, MyReceiver.class);
                Intent notificationEndIntent = new Intent(AddAssessment.this, MyReceiver.class);

                notificationStartIntent.putExtra("key", "Assessment starts today: " + assessmentTitle.getText());
                notificationEndIntent.putExtra("key", assessmentTitle.getText() + " Assessment Ends today.");

                //Pending intents
                PendingIntent startTime = PendingIntent.getBroadcast(AddAssessment.this, MainActivity.alertNum++, notificationStartIntent, 0);
                PendingIntent endTime = PendingIntent.getBroadcast(AddAssessment.this, MainActivity.alertNum++, notificationEndIntent, 0);

                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManagerStart.set(AlarmManager.RTC_WAKEUP,alertStartTime, startTime);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP,alertEndTime, endTime);

                Toast.makeText(this, "Assessment Saved, Check Database.", Toast.LENGTH_LONG).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

   //Used for testing date conversions.  This is a working model.
    public void test(View view) throws ParseException {

        SimpleDateFormat test = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
        Date testStartDate = test.parse(getStart.getText().toString());
        Date testEndDate = test.parse(getEnd.getText().toString());
        if (testStartDate.before(testEndDate)) {
            Log.println(Log.INFO,"debug", "Victory");
        } else {
            Log.println(Log.INFO,"debug", "Fail");
        }

    }
}