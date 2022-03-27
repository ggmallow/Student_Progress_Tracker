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
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM courses ORDER BY courseID ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE courseID = :courseID")
            Course getCourseByID(Integer courseID);

}
