package com.example.studenttracker.DAO;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Term;

import java.util.List;

public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAssessment(Assessment assessment);

    @Update
    void updateCourse(Assessment assessment);

    @Delete
    void deleteCourse(Assessment assessment);

    @Query("SELECT * FROM assessments ORDER BY assessmentID ASC")
    List<Term> getAllAssessments();

}
