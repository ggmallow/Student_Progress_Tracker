package com.example.studenttracker.Database;

import android.app.Application;

import com.example.studenttracker.DAO.AssessmentDAO;
import com.example.studenttracker.DAO.CourseDAO;
import com.example.studenttracker.DAO.TermDAO;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {


    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private AssessmentDAO mAssessmentDAO;

    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    public List<Assessment> mAllAssessments;
    public List<Assessment> mAvailableAssessments;
    public Course testCourse;


    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {

        StudentTrackerDatabaseBuilder db = StudentTrackerDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();

    }
//This section contains the methods for Terms

    //This inserts a term into the database.
    public void insertTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.insertTerm(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will update a term.
    public void updateTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.updateTerm(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will delete a term.
    public void deleteTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.deleteTerm(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will get all terms.
    public List<Term>getAllTerms() {
        databaseExecutor.execute(()->{
            mAllTerms = mTermDAO.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

//This section of code will be for Assessments

    //This inserts a Assessment into the database.
    public void insertAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.insertAssessment(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will update a Assessment.
    public void updateAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.updateAssessment(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will delete a Assessment.
    public void deleteAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.deleteAssessment(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will get all Assessments.
    public List<Assessment>getAllAssessments() {
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    public List<Assessment>getAvailableAssessments() {
        databaseExecutor.execute(()->{
            mAvailableAssessments = mAssessmentDAO.getAvailableAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAvailableAssessments;
    }



//This section of code will be for Courses.

//This inserts a Course into the database.
public void insertCourse(Course course){
    databaseExecutor.execute(()->{
        mCourseDAO.insertCourse(course);
    });
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

    //This will update a Course.
    public void updateCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.updateCourse(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will delete a Course.
    public void deleteCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.deleteCourse(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //This will get all Courses.
    public List<Course>getAllCourses() {
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    public Course getCourseByID(Integer courseID) {

        databaseExecutor.execute(()->{
            testCourse = mCourseDAO.getCourseByID(courseID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return testCourse;
    }

}
