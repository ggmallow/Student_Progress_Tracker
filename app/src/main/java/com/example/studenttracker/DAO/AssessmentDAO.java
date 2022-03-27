package com.example.studenttracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Term;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAssessment(Assessment assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("SELECT * FROM assessments ORDER BY assessmentID ASC")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE courseID IS null")
    List<Assessment> getAvailableAssessments();

}
