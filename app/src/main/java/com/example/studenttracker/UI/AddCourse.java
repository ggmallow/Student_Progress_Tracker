package com.example.studenttracker.UI;

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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Instructor;
import com.example.studenttracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener , AssessmentAdapter.assessmentClickListener, AssessmentAdapter2.assessmentClickListener2{

    public DatePickerDialog.OnDateSetListener dateSetListener;
    public DatePickerDialog datePickerDialog;
    public DatePickerDialog datePickerDialog2;

    private TextView courseTitle; //Setting Course Title.
    private TextView getStart; //Setting Start Date.
    private TextView getEnd; //Setting End Date.
    public TextView detailsInfo;
    public TextView courseNotes;

    public Spinner statusSpinner; //Setting up Spinner for Status.
    public ArrayAdapter statusAdapter;

    public Spinner instructorSpinner; //Setting up Spinner for Instructors.
    public ArrayAdapter instructorAdapter;
    public ArrayList<Instructor> instructorList = new ArrayList<>();

    public TextView instructorPhone; //Setting up Instructor Phone TextView.
    public TextView instructorEmail; //Setting up Instructor Email TextView.

    public Button saveCourse;

    public Button attachAssessment;
    public Button detachAssessment;

    public Button shareNotes;

    //Used for modding course
    //public Bundle moddingCourse;
    public ArrayList<Course> allCourses;
    public int modID;
    public Integer modCourseID;

    //Used for detailed View

    //Setting up for selecting Assessments.
    public ArrayList<Assessment> availableAssessments = new ArrayList<>();
    public ArrayList<Assessment> assessmentsAttached = new ArrayList<>();
    //public ArrayList<Assessment> tempAssessmentsAttached = new ArrayList<>();
    public RecyclerView allAssessmentsRecycler;

    public RecyclerView assessmentsToCompleteRecycler;
    public AssessmentAdapter2 assessmentAdapter2; // Borrowed adapter from Assessment activity, to display data properly.

    public Assessment selectedAssessment; // Used to pick courses in Recycler View
    public AssessmentAdapter assessmentAdapter; // Borrowed adapter from Assessment activity, to display data properly.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        getStart = findViewById(R.id.startDate);
        getEnd = findViewById(R.id.endDate);
        courseNotes = findViewById(R.id.courseNotes);
        attachAssessment = findViewById(R.id.attachAssessment);
        detachAssessment = findViewById(R.id.detachAssessment);
        shareNotes = findViewById(R.id.shareNotes);
        shareNotes.setVisibility(View.GONE); //Hiding unless coming from Course Details button.
        detailsInfo = findViewById(R.id.detailsInfo);
        detailsInfo.setVisibility(View.GONE);
        courseTitle = findViewById(R.id.courseTitle); //Setting Course Title.
        statusSpinner = findViewById(R.id.status); //Setting up Spinner for Status.
        instructorSpinner = findViewById(R.id.instructor); //Setting up Spinner for Instructors.
        instructorPhone = findViewById(R.id.instructorPhone); //Setting up Instructor Phone TextView.
        instructorEmail = findViewById(R.id.instructorEmail); //Setting up Instructor Email TextView.
        saveCourse = findViewById(R.id.saveCourse);
        instructorPhone = findViewById(R.id.instructorPhone); //Setting up Instructor Phone TextView.
        instructorEmail = findViewById(R.id.instructorEmail); //Setting up Instructor Email TextView.


        initStartDatePicker(); // Sets up the date picker for Start Date
        initEndDatePicker(); //Sets up the date picker for End Date.

        //Setting up Status Spinner.
        statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        //Adding instructors
        Instructor testInstructor = new Instructor("John", 1234561234, "test@wgu.edu");
        instructorList.add(testInstructor);
        Instructor testInstructor2 = new Instructor("Matt", 189123456, "test2@wgu.edu");
        instructorList.add(testInstructor2);
        initInstructorPicker(); //Loads menu with instructors.

        availableAssessments = new ArrayList<Assessment>(); //Initiating new ArrayList

        assessmentAdapter = new AssessmentAdapter(availableAssessments, this); // Change allAssessments to array of courses attached.
        allAssessmentsRecycler = findViewById(R.id.attachedAssessments);
        RecyclerView.LayoutManager assessmentLayout = new LinearLayoutManager(getApplicationContext());
        allAssessmentsRecycler.setLayoutManager(assessmentLayout);
        allAssessmentsRecycler.setItemAnimator(new DefaultItemAnimator());
        allAssessmentsRecycler.setAdapter(assessmentAdapter);

        //Setting up for Selected Assessments
        assessmentsAttached = new ArrayList<>();
        assessmentAdapter2 = new AssessmentAdapter2(assessmentsAttached, this);
        assessmentsToCompleteRecycler = findViewById(R.id.assessmentsToComplete);
        RecyclerView.LayoutManager assessmentLayout2 = new LinearLayoutManager(getApplicationContext());
        assessmentsToCompleteRecycler.setLayoutManager(assessmentLayout2);
        assessmentsToCompleteRecycler.setItemAnimator(new DefaultItemAnimator());
        assessmentsToCompleteRecycler.setAdapter(assessmentAdapter2);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getInt("detailView") == 1) {
                disableUI(); //Disabling UI if coming from Course Details button.
            }
            if (getIntent().getExtras().getInt("courseID") > 0) {
                courseDetailsInit(); //Setting up if navigating form Course Details button. Disabled as it is being ran in disableUI();
            }
        }
      //  courseDetailsInit(); //Setting up if navigating form Course Details button. Disabled as it is being ran in disableUI();



        Handler testHandler = new Handler();
        getAvailableAssessments(new GetAllAssessmentsCallback() {
            @Override
            public void onComplete(List<Assessment> assessments) {
                testHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        availableAssessments.addAll(assessments);
                        assessmentAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


    }

    interface GetAllAssessmentsCallback {
        void onComplete(List<Assessment> assessments);
    }

    private void getAvailableAssessments(GetAllAssessmentsCallback callback) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Repository repo = new Repository(getApplication()); //Creating new Repository to get Courses.
                List<Assessment> assessments = repo.getAvailableAssessments();
                callback.onComplete(assessments);


            }
        });

    }

    //Disables UI to prevent the user from editing fields.
    private void disableUI() {

                courseTitle.setEnabled(false);
                getStart.setEnabled(false);
                getEnd.setEnabled(false);
                statusSpinner.setEnabled(false);
                instructorSpinner.setEnabled(false);
                instructorPhone.setEnabled(false);
                instructorEmail.setEnabled(false);
                attachAssessment.setEnabled(false);
                detachAssessment.setEnabled(false);
                courseNotes.setEnabled(false);
                shareNotes.setVisibility(View.VISIBLE); //Hiding unless coming from Course Details button.
                saveCourse.setVisibility(View.GONE);
                detailsInfo.setVisibility(View.VISIBLE);


    }

    private void courseDetailsInit() {
        try {

                Integer courseID = getIntent().getExtras().getInt("courseID");


                Repository repo = new Repository(getApplication());

                Course modifiedCourse = repo.getCourseByID(courseID);


                courseTitle.setText(modifiedCourse.getTitle());
                getStart.setText(modifiedCourse.getStartDate());
                getEnd.setText(modifiedCourse.getEndDate());
                modID = modifiedCourse.getCourseID();


                statusSpinner.setSelection(statusAdapter.getPosition(modifiedCourse.getStatus()));
                courseNotes.setText(modifiedCourse.getCourseNotes());


                //Setting up Attached Assessments box.
                assessmentsAttached.addAll(repo.getAllAssessments());//Use for temporary list, so original isn't modified.
                //Add loop for null foreign key on Assessments
                ArrayList<Assessment> tempAssessmentsAttachedCopy = new ArrayList<>(); // Temporary array to hold all assessments with a null courseID;
                for (Assessment nullCourse: assessmentsAttached) {
                    if (nullCourse.getCourseID() == modifiedCourse.getCourseID()) {
                        tempAssessmentsAttachedCopy.add(nullCourse);
                    }

                }
                assessmentsAttached.clear(); //Clear tempAssessmentsAttached so values reflect accurately.
                assessmentsAttached.addAll(tempAssessmentsAttachedCopy); //Setting to mpAssessmentsAttached to match the tempAssessmentsAttachedCopy(all assessments with no course attached)




        } catch (Exception e) {
            e.printStackTrace();
        }

    }
/*
    //Method to set up fields if coming from the Course edit button.
    private void modCourseInit() {

        if (getIntent().getExtras() != null) {

            allCourses = new ArrayList<Course>();

            Integer courseID = getIntent().getExtras().getInt("courseDetails");

            Repository repo = new Repository(getApplication());
            Course modifiedCourse = repo.getCourseByID(courseID);


            courseTitle.setText(modifiedCourse.getTitle());
            getStart.setText(modifiedCourse.getStartDate());
            getEnd.setText(modifiedCourse.getEndDate());
            modID = modifiedCourse.getCourseID();


            statusSpinner.setSelection(statusAdapter.getPosition(modifiedCourse.getStatus()));
            courseNotes.setText(modifiedCourse.getCourseNotes());


            //Setting up Attached Assessments box.
            assessmentsAttached.addAll(repo.getAllAssessments());//Use for temporary list, so original isn't modified.
            //Add loop for null foreign key on Assessments
            ArrayList<Assessment> tempAssessmentsAttachedCopy = new ArrayList<>(); // Temporary array to hold all assessments with a null courseID;
            for (Assessment nullCourse: assessmentsAttached) {
                if (nullCourse.getCourseID() == modifiedCourse.getCourseID()) {
                    tempAssessmentsAttachedCopy.add(nullCourse);
                }

            }
            assessmentsAttached.clear(); //Clear tempAssessmentsAttached so values reflect accurately.
            assessmentsAttached.addAll(tempAssessmentsAttachedCopy); //Setting to mpAssessmentsAttached to match the tempAssessmentsAttachedCopy(all assessments with no course attached)

        }
    }
*/
    private void initInstructorPicker() {
        //Setting up instructor box.
        instructorAdapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_spinner_item, instructorList);

        instructorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructorSpinner.setAdapter(instructorAdapter);

        instructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Instructor selectedInstructor = (Instructor) adapterView.getItemAtPosition(i);
                instructorPhone.setText(String.valueOf(selectedInstructor.getPhone()));
                instructorEmail.setText(selectedInstructor.getEmail());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

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

    public void saveCourse(View view) throws ParseException {

        try {
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
                return;
            }

            // Setting a date formatter to convert strings to actual Dates. https://www.baeldung.com/java-string-to-date
            SimpleDateFormat date = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
            Date startDate = date.parse(getStart.getText().toString()); // Converting String, getStart to startDate as a Date Object.
            Date endDate = date.parse(getEnd.getText().toString()); // Converting String, getEnd to endDate as a Date Object.

            //Making sure Start Date is before the End Date
            if (endDate.before(startDate)) {
                Toast.makeText(this, "Course End Date must be after Start Date.", Toast.LENGTH_LONG).show();
                return;
            }
            //Not allowing for Start Date to be equal to End Date.
            if (startDate.equals(endDate)) {
                Toast.makeText(this, "Course Start Date can not equal End Date.", Toast.LENGTH_LONG).show();

            }

            else if (getIntent().getExtras() != null) {

                Repository repo = new Repository(getApplication());

                for (Assessment attachingCourse: assessmentsAttached) {
                    Assessment modAssessment = new Assessment(attachingCourse.getAssessmentID(),
                            attachingCourse.getAssessmentType(),
                            attachingCourse.getAssessmentTitle(),
                            attachingCourse.getStartDate(),
                            attachingCourse.getEndDate(),modCourseID);
                    repo.updateAssessment(modAssessment);

                }

               /* for (Assessment detachingCourse: tempAssessmentsAttached) {
                    Assessment modAssessment = new Assessment(detachingCourse.getAssessmentID(),
                            detachingCourse.getAssessmentType(),
                            detachingCourse.getAssessmentTitle(),
                            detachingCourse.getStartDate(),
                            detachingCourse.getEndDate(),null);
                    repo.updateAssessment(modAssessment);

                }*/

                Course modCourse = new Course(modID,
                        courseTitle.getText().toString(),
                        getStart.getText().toString(),
                        getEnd.getText().toString(),
                        statusSpinner.getSelectedItem().toString(),
                        instructorSpinner.getSelectedItem().toString(),
                        courseNotes.getText().toString(), null
                        );
                repo.updateCourse(modCourse);


                Long alertStartTime = startDate.getTime();
                Long alertEndTime = endDate.getTime();

                Intent notificationStartIntent = new Intent(AddCourse.this, MyReceiver.class);
                Intent notificationEndIntent = new Intent(AddCourse.this, MyReceiver.class);

                notificationStartIntent.putExtra("key", "Course starts today: " + courseTitle.getText());
                notificationEndIntent.putExtra("key", courseTitle.getText() + " Course Ends today.");

                //Pending intents
                PendingIntent startTime = PendingIntent.getBroadcast(AddCourse.this, MainActivity.alertNum++, notificationStartIntent, 0);
                PendingIntent endTime = PendingIntent.getBroadcast(AddCourse.this, MainActivity.alertNum++, notificationEndIntent, 0);

                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManagerStart.set(AlarmManager.RTC_WAKEUP,alertStartTime, startTime);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP,alertEndTime, endTime);


                Toast.makeText(this, "Modification Complete.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddCourse.this,Courses.class);
                startActivity(intent);
            }else {

                Repository repo = new Repository(getApplication());

                Course newCourse = new Course(null,
                        courseTitle.getText().toString(),
                        getStart.getText().toString(),
                        getEnd.getText().toString(),
                        statusSpinner.getSelectedItem().toString(),
                        instructorSpinner.getSelectedItem().toString(),
                        courseNotes.getText().toString(),null);
                repo.insertCourse(newCourse);

                allCourses = new ArrayList<Course>();
                allCourses.addAll(repo.getAllCourses());
                int maxID = allCourses.get(0).getCourseID();

                for (int i=1; i<allCourses.size(); i++) {
                    if (allCourses.get(i).getCourseID() > maxID) {
                        maxID = allCourses.get(i).getCourseID();
                    }

                }

                //Update all Assessments in assessmentsAttached to hold course ID found.
                for (Assessment attachingCourse: assessmentsAttached) {
                    Assessment modAssessment = new Assessment(attachingCourse.getAssessmentID(),
                            attachingCourse.getAssessmentType(),
                            attachingCourse.getAssessmentTitle(),
                            attachingCourse.getStartDate(),
                            attachingCourse.getEndDate(),maxID);
                    repo.updateAssessment(modAssessment);

                }


                Long alertStartTime = startDate.getTime();
                Long alertEndTime = endDate.getTime();

                Intent notificationStartIntent = new Intent(AddCourse.this, MyReceiver.class);
                Intent notificationEndIntent = new Intent(AddCourse.this, MyReceiver.class);

                notificationStartIntent.putExtra("key", "Course starts today: " + courseTitle.getText());
                notificationEndIntent.putExtra("key", courseTitle.getText() + " Course Ends today.");

                //Pending intents
                PendingIntent startTime = PendingIntent.getBroadcast(AddCourse.this, MainActivity.alertNum++, notificationStartIntent, 0);
                PendingIntent endTime = PendingIntent.getBroadcast(AddCourse.this, MainActivity.alertNum++, notificationEndIntent, 0);

                AlarmManager alarmManagerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManagerStart.set(AlarmManager.RTC_WAKEUP,alertStartTime, startTime);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP,alertEndTime, endTime);


                Toast.makeText(this, "Course Saved.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddCourse.this,Courses.class);
                startActivity(intent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void shareNotes(View view) {
      String sharedNote = courseNotes.getText().toString();
      Intent sharedIntent = new Intent();
      sharedIntent.setAction(Intent.ACTION_SEND);
      sharedIntent.putExtra(Intent.EXTRA_TEXT, sharedNote);
      sharedIntent.putExtra(Intent.EXTRA_TITLE, "Course Notes");
      sharedIntent.setType("text/plain");
      Intent sharedIntentChooser = Intent.createChooser(sharedIntent,null);
      startActivity(sharedIntentChooser);
    }

    public void attachAssessment(View view) {
        if (assessmentAdapter.checkedPosition == -1) {
            Toast.makeText(this,"You must select an assessment.",Toast.LENGTH_LONG).show();

        } else {
           assessmentsAttached.add(selectedAssessment);
           assessmentAdapter2.notifyDataSetChanged();
        //   tempAssessmentsAttached.remove(assessmentAdapter.checkedPosition);
           assessmentAdapter.notifyDataSetChanged();
           assessmentAdapter.checkedPosition = -1;
        }

    }

    public void detachAssessment(View view) {

        if (assessmentAdapter2.checkedPosition == -1) {
            Toast.makeText(this, "You must select an assessment.", Toast.LENGTH_LONG).show();

        } else {
         //  tempAssessmentsAttached.add(selectedAssessment);
           assessmentAdapter.notifyDataSetChanged();
           assessmentsAttached.remove(assessmentAdapter2.checkedPosition);
           assessmentAdapter2.notifyDataSetChanged();
        }

    }

    //Used for OnItemSelected Listener.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onAssessmentClick(int position) {
        courseTitle.clearFocus(); //Clearing focus to fix UI skipping
        courseNotes.clearFocus();
        selectedAssessment = new Assessment(availableAssessments.get(position).getAssessmentID(),
                availableAssessments.get(position).getAssessmentType(),
                availableAssessments.get(position).getAssessmentTitle(),
                availableAssessments.get(position).getStartDate(),
                availableAssessments.get(position).getEndDate(),
                null);
    }

    @Override
    public void onAssessmentClick2(int position) {
        courseTitle.clearFocus(); //Clearing focus to fix UI skipping
        courseNotes.clearFocus();
        selectedAssessment = new Assessment(assessmentsAttached.get(position).getAssessmentID(),
                assessmentsAttached.get(position).getAssessmentType(),
                assessmentsAttached.get(position).getAssessmentTitle(),
                assessmentsAttached.get(position).getStartDate(),
                assessmentsAttached.get(position).getEndDate(),
                null);
    }
}
