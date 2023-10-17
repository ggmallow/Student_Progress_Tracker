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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    //Used for date picking
    public DatePickerDialog.OnDateSetListener dateSetListener;
    public DatePickerDialog datePickerDialog;
    public DatePickerDialog datePickerDialog2;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        assessmentType = findViewById(R.id.assessmentType);
        performance = findViewById(R.id.performance);
        objective = findViewById(R.id.objective);
        assessmentTitle = findViewById(R.id.assessmentTitle);
        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);
        saveAssessment = findViewById(R.id.saveAssessment);
        detailsInfo = findViewById(R.id.detailsInfo);
        detailsInfo.setVisibility(View.GONE);

        initStartDatePicker(); // Sets up the date picker for Start Date
        initEndDatePicker(); //Sets up the date picker for End Date.

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getInt("detailView") == 1) {
                disableUI();
            }

        if (getIntent().getExtras().getInt("assessmentID") > 0) {
            assessmentDetailsInit(); //Setting up for Modding an Assessment.
        }
    }

    }

    private void disableUI() {
        assessmentType.setEnabled(false);
        performance.setEnabled(false);
        objective.setEnabled(false);
        assessmentTitle.setEnabled(false);
        getStart.setEnabled(false);
        getEnd.setEnabled(false);
        saveAssessment.setVisibility(View.GONE);
        detailsInfo.setVisibility(View.VISIBLE);
    }

    //Method populates form with data.
    private void assessmentDetailsInit() {

        Repository repo = new Repository(getApplication());
        int assessmentID = getIntent().getExtras().getInt("assessmentID");
        Assessment modifiedAssessment = repo.getAssessmentByID(assessmentID);

        if ("Performance".equals(modifiedAssessment.getAssessmentType())) {
            performance.setChecked(true);
        } else {
            objective.setChecked(true);
        }
        assessmentTitle.setText(modifiedAssessment.getAssessmentTitle());
        getStart.setText(modifiedAssessment.getStartDate());
        getEnd.setText(modifiedAssessment.getEndDate());

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

        try {
            if (assessmentType.getCheckedRadioButtonId() == -1) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You must select the Assessment Type.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                return;
            }

            if (performance.isChecked()) {
                tempAssessmentType = "Performance";
            } else if (objective.isChecked()) {
                tempAssessmentType = "Objective";
            }

            if (assessmentTitle.getText().toString().isEmpty()) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You haven't entered a title.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                return;
            }
            if (assessmentTitle.getText().toString().length() > 25) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Please choose a title 25 or less characters.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                return;
            }
            if (getStart.getText().toString().isEmpty()) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You must pick a Start Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                return;
            }

            if (getEnd.getText().toString().isEmpty()) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You must pick a End Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                return;
            }
            // Setting a date formatter to convert strings to actual Dates. https://www.baeldung.com/java-string-to-date
            SimpleDateFormat date = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
            Date startDate = date.parse(getStart.getText().toString()); // Converting String, getStart to startDate as a Date Object.
            Date endDate = date.parse(getEnd.getText().toString()); // Converting String, getEnd to endDate as a Date Object.

            //Making sure Start Date is before the End Date
            if (endDate.before(startDate)) {


                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Assessment End Date must be after Start Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

            }
            //Not allowing for Start Date to be equal to End Date.
            /* disabled as I think that a assessment should be able to end on the same day.
            if (startDate.equals(endDate)) {
                Toast.makeText(this, "Assessment Start Date can not equal End Date.", Toast.LENGTH_LONG).show();
            } */

            else if (getIntent().getExtras() != null) {

                Repository repo = new Repository(getApplication());
                Assessment getCourseID = repo.getAssessmentByID(getIntent().getExtras().getInt("assessmentID"));
                Assessment modAssessment = new Assessment(
                        getIntent().getExtras().getInt("assessmentID"),
                        tempAssessmentType,
                        assessmentTitle.getText().toString(),
                        getStart.getText().toString(),
                        getEnd.getText().toString(), getCourseID.getCourseID());
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

                Toast.makeText(this, "Modification Complete.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddAssessment.this,Assessments.class);
                startActivity(intent);
            }else {

                Repository repo = new Repository(getApplication());
                Assessment newAssessment = new Assessment(
                        null,
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

                Toast.makeText(this, "Assessment Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddAssessment.this,Assessments.class);
                startActivity(intent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
