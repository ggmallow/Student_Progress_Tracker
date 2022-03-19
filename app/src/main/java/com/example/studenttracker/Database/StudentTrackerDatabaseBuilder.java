package com.example.studenttracker.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studenttracker.DAO.AssessmentDAO;
import com.example.studenttracker.DAO.CourseDAO;
import com.example.studenttracker.DAO.TermDAO;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;

@Database(entities = {Term.class,  Course.class, Assessment.class}, version = 4, exportSchema = false)

public abstract class StudentTrackerDatabaseBuilder extends RoomDatabase {

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile StudentTrackerDatabaseBuilder INSTANCE;

    static StudentTrackerDatabaseBuilder getDatabase(final Context context) {

        if (INSTANCE == null) {

        synchronized (StudentTrackerDatabaseBuilder.class) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StudentTrackerDatabaseBuilder.class, "studentTracker.db")
                    //If wanting to run query's on MainThread add this but it will lock up the main system.
                    //.allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            }
        }
    }
    return INSTANCE;
    }

}
