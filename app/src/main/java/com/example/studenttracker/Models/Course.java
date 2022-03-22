package com.example.studenttracker.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseID;

    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String instructor;
    private String courseNotes;
    //private ArrayList attachedAssessments; // If problems saving, it might be because Integer vs int.


    public Course(int courseID, String title, String startDate, String endDate,String status, String instructor, String courseNotes /* ArrayList attachedAssessments */) {
        this.courseID = courseID;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructor = instructor;
        this.courseNotes = courseNotes;
       // this.attachedAssessments = attachedAssessments;
    }
/* This may not even be needed.
    public ArrayList<Integer> getAttachedAssessments() {
        return attachedAssessments;
    }

    public void setAttachedAssessments(ArrayList<Integer> attachedAssessments) {
        this.attachedAssessments = attachedAssessments;
    }
    //This may be used but, I am handling objects different than I am used too.
   /* public void addAssociatedAssessment(Assessment selectedAssessment) {
        attachedAssessments.add(selectedPart.getAssessmentID);
    } */

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }
/*
    public ArrayList<Integer> getAttachedAssessments() {
        return attachedAssessments;
    }

    public void setAttachedAssessments(ArrayList attachedAssessments) {
        this.attachedAssessments = attachedAssessments;
    }
    */

}
