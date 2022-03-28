package com.example.studenttracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTerm(Term term);

    @Update
    void updateTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("SELECT * FROM terms ORDER BY termID ASC")
     List<Term> getAllTerms();

    @Query("SELECT * FROM terms WHERE termID = :termID")
    Term getTermByID(Integer termID);
}
