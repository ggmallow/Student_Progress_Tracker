package com.example.studenttracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

//Does this model require a startDate?

@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = Course.class,parentColumns = "courseID", childColumns = "courseID"))
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private Integer assessmentID;

    private String assessmentType;
    private String assessmentTitle;
    private String startDate;
    private String endDate;
    private Integer courseID;

    public Assessment(Integer assessmentID, String assessmentType, String assessmentTitle, String startDate, String endDate, Integer courseID) {
        this.assessmentID = assessmentID;
        this.assessmentType = assessmentType;
        this.assessmentTitle = assessmentTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseID = courseID;
    }

    public Integer getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(Integer assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
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

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID=" + assessmentID +
                ", assessmentType='" + assessmentType + '\'' +
                ", assessmentTitle='" + assessmentTitle + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
