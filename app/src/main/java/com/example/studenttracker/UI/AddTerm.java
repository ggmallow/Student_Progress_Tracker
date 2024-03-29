package com.example.studenttracker.UI;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class AddTerm extends AppCompatActivity implements CourseAdapter.OnCourseListener, CourseAdapter2.OnCourseListener2{

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
    public CourseAdapter courseAdapter; // Borrowed adapter from Courses activity, to display data properly.

    //Setting up Courses being Taken
    public ArrayList<Course> allCoursesEnrolled = new ArrayList<>();
    public RecyclerView attachedCoursesRecycler;
    public CourseAdapter2 courseAdapter2;

    public ArrayList<Course> allCoursesTemp = new ArrayList<>(); // Used to keep allCourse list complete, while adding courses.
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


        allCoursesTemp = new ArrayList<Course>(); // Initiating temp array to hold course list.
        allCoursesTemp.addAll(repo.getAllCourses()); // Getting all courses.


        courseAdapter = new CourseAdapter(allCoursesTemp, this); // Change allCourses to array of courses attached.
        allCoursesRecycler = findViewById(R.id.attachedCourses);
        RecyclerView.LayoutManager courseLayout = new LinearLayoutManager(getApplicationContext());
        allCoursesRecycler.setLayoutManager(courseLayout);
        allCoursesRecycler.setItemAnimator(new DefaultItemAnimator());
        allCoursesRecycler.setAdapter(courseAdapter);


        //Setting up Courses being Taken
        allCoursesEnrolled = new ArrayList<Course>(); // Initiating courses enrolled.
        courseAdapter2 = new CourseAdapter2(allCoursesEnrolled,this);
        attachedCoursesRecycler = findViewById(R.id.attachedCoursesRecycler);
        RecyclerView.LayoutManager courseLayout2 = new LinearLayoutManager(getApplicationContext());
        attachedCoursesRecycler.setLayoutManager(courseLayout2);
        attachedCoursesRecycler.setItemAnimator(new DefaultItemAnimator());
        attachedCoursesRecycler.setAdapter(courseAdapter2);

        //Looking for null values attached to courses.
        ArrayList<Course> tempCourseAttachedCopy = new ArrayList<>(); // Temporary array to hold all courses with a null termID;
        for (Course nullTerm: allCoursesTemp) {
            if (nullTerm.getTermID() == null) {
                tempCourseAttachedCopy.add(nullTerm);
            }

        }
        allCoursesTemp.clear(); //Clear tempAssessmentsAttached so values reflect accurately.
        allCoursesTemp.addAll(tempCourseAttachedCopy); //Setting to mpAssessmentsAttached to match the tempAssessmentsAttachedCopy(all assessments with no course attached)

        if (getIntent().getExtras() != null ) {
            if (getIntent().getExtras().getInt("detailView") == 1) {
                disableUI();
            }

            if (getIntent().getExtras().getInt("termID") > 0) {
                termDetailsInit();
            }
        }

    }

    private void disableUI() {
        //Disabling all fields to prevent editing
        termName.setEnabled(false);
        getStart.setEnabled(false);
        getEnd.setEnabled(false);
        allCoursesRecycler.setEnabled(false);
        attachCourse.setEnabled(false);
        detachCourse.setEnabled(false);
        saveTerm.setVisibility(View.GONE);
        detailsInfo.setVisibility(View.VISIBLE);
    }

    private void termDetailsInit() {
        try {

            Repository repo = new Repository(getApplication());
            Term modifiedTerm = repo.getTermByID(getIntent().getExtras().getInt("termID"));

            termName.setText(modifiedTerm.getTitle());
            getStart.setText(modifiedTerm.getStartDate());
            getEnd.setText(modifiedTerm.getEndDate());

            ArrayList<Course> tempCourse = new ArrayList();
            for (Course nullTerm: allCourses) {
                if (nullTerm.getTermID() == modifiedTerm.getTermID()) {
                    tempCourse.add(nullTerm);

                }
            }
            allCoursesEnrolled.clear();
            allCoursesEnrolled.addAll(tempCourse);

        } catch (Exception e) {
            //e.printStackTrace();
            Log.println(Log.INFO,"debug", "Null pointer has occurred.");
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

        try {
            if (termName.getText().toString().isEmpty()) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You haven't entered a Term Name");

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
                text.setText("You haven't set a Start Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                //Add this stuff if we have time, it will need an additional method to clear colors if selected(focus).
                /*getStart.getBackground().setTint(Color.parseColor("#FF9494"));
                getStart.setHintTextColor(Color.parseColor("#FF9494"));*/
                return;
            }

            if (getEnd.getText().toString().isEmpty()) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You haven't set a End Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                return;

            }
            // Setting a date formatter to convert strings to actual Dates. https://www.baeldung.com/java-string-to-date
            SimpleDateFormat test = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
            Date startDate = test.parse(getStart.getText().toString()); // Converting String, getStart to startDate as a Date Object.
            Date endDate = test.parse(getEnd.getText().toString()); // Converting String, getEnd to endDate as a Date Object.

            //Making sure Start Date is before the End Date
            if (endDate.before(startDate)) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Term End Date must be after Start Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();


                return;
            }
            //Not allowing for Start Date to be equal to End Date.
            if (startDate.equals(endDate)) {


                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Term Start Date can not equal End Date.");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();


            } else if (getIntent().getExtras() != null) {

                Repository repo = new Repository(getApplication());




                for (Course attachingTerm: allCoursesEnrolled) {
                    Course modCourse = new Course(attachingTerm.getCourseID(),
                            attachingTerm.getTitle(),
                            attachingTerm.getStartDate(),
                            attachingTerm.getEndDate(),
                            attachingTerm.getStatus(),
                            attachingTerm.getInstructor(),
                            attachingTerm.getCourseNotes(),
                            getIntent().getExtras().getInt("termID"));
                    repo.updateCourse(modCourse);

                }

                for (Course detachTerm: allCoursesTemp) {
                    Course modCourse = new Course(detachTerm.getCourseID(),
                            detachTerm.getTitle(),
                            detachTerm.getStartDate(),
                            detachTerm.getEndDate(),
                            detachTerm.getStatus(),
                            detachTerm.getInstructor(),
                            detachTerm.getCourseNotes(),
                            null);
                    repo.updateCourse(modCourse);

                }



                Term modTerm = new Term(
                        getIntent().getExtras().getInt("termID"),
                        termName.getText().toString(),
                        getStart.getText().toString(),
                        getEnd.getText().toString());
                repo.updateTerm(modTerm);


                Long alertStartTime = startDate.getTime();
                Long alertEndTime = endDate.getTime();

                Intent notificationStartIntent = new Intent(AddTerm.this, MyReceiver.class);
                Intent notificationEndIntent = new Intent(AddTerm.this, MyReceiver.class);

                notificationStartIntent.putExtra("key", "Term starts today: " + termName.getText());
                notificationEndIntent.putExtra("key", termName.getText() + ": Term Ends today.");

                //Pending intents
                PendingIntent startTime = PendingIntent.getBroadcast(AddTerm.this, MainActivity.alertNum++, notificationStartIntent, 0);
                PendingIntent endTime = PendingIntent.getBroadcast(AddTerm.this, MainActivity.alertNum++, notificationEndIntent, 0);

                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManagerStart.set(AlarmManager.RTC_WAKEUP,alertStartTime, startTime);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP,alertEndTime, endTime);


                Toast.makeText(this, "Modification Complete.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddTerm.this,Terms.class);
                startActivity(intent);
            } else {
                Repository repo = new Repository(getApplication());
                Term newTerm = new Term(null,
                        termName.getText().toString(),
                        getStart.getText().toString(),
                        getEnd.getText().toString());
                repo.insertTerm(newTerm);



                ///We are going to associate Courses with terms here
                ArrayList<Term> allTerms = new ArrayList<Term>();
                allTerms.addAll(repo.getAllTerms());
                int maxID = allTerms.get(0).getTermID();

                for (int i=1; i<allTerms.size(); i++) {
                    if (allTerms.get(i).getTermID() > maxID) {
                        maxID = allTerms.get(i).getTermID();
                    }

                }
                Log.println(Log.INFO,"debug", "Max ID is: " + maxID);

                for (Course attachingTerm: allCoursesEnrolled) {
                    Course modCourse = new Course(attachingTerm.getCourseID(),
                            attachingTerm.getTitle(),
                            attachingTerm.getStartDate(),
                            attachingTerm.getEndDate(),
                            attachingTerm.getStatus(),
                            attachingTerm.getInstructor(),
                            attachingTerm.getCourseNotes(),
                            maxID);
                    repo.updateCourse(modCourse);

                }

                Long alertStartTime = startDate.getTime();
                Long alertEndTime = endDate.getTime();

                Intent notificationStartIntent = new Intent(AddTerm.this, MyReceiver.class);
                Intent notificationEndIntent = new Intent(AddTerm.this, MyReceiver.class);

                notificationStartIntent.putExtra("key", "Term starts today: " + termName.getText());
                notificationEndIntent.putExtra("key", termName.getText() + " Term Ends today.");

                //Pending intents
                PendingIntent startTime = PendingIntent.getBroadcast(AddTerm.this, MainActivity.alertNum++, notificationStartIntent, 0);
                PendingIntent endTime = PendingIntent.getBroadcast(AddTerm.this, MainActivity.alertNum++, notificationEndIntent, 0);

                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManagerStart.set(AlarmManager.RTC_WAKEUP,alertStartTime, startTime);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP,alertEndTime, endTime);

                Toast.makeText(this, "Term Saved.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddTerm.this,Terms.class);
                startActivity(intent);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void attachCourse(View view) {

        if (courseAdapter.checkedPosition == -1) {

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must select an assessment.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        } else {
            allCoursesEnrolled.add(selectedCourse);
            courseAdapter2.notifyDataSetChanged();
            allCoursesTemp.remove(courseAdapter.checkedPosition);
            courseAdapter.notifyDataSetChanged();
            courseAdapter.checkedPosition = -1;
        }

    }

    public void detachCourse(View view) {
        if (courseAdapter2.checkedPosition == -1) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must select an assessment.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            allCoursesTemp.add(selectedCourse);
            courseAdapter.notifyDataSetChanged();
            allCoursesEnrolled.remove(courseAdapter2.checkedPosition);
            courseAdapter2.notifyDataSetChanged();
            courseAdapter2.checkedPosition = -1;
        }

    }

    @Override
    public void onCourseClick(int position) {
        termName.clearFocus(); //Clearing focus to fix UI skipping
        selectedCourse = allCoursesTemp.get(position);
    }

    @Override
    public void onCourseClick2(int position) {
        termName.clearFocus(); //Clearing focus to fix UI skipping
        selectedCourse = allCoursesEnrolled.get(position);
    }

}
