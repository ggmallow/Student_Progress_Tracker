package com.example.studenttracker.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

//Does this model require a startDate?

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentID;

    private String assessmentType;
    private String assessmentTitle;
    private String endDate;

    public Assessment(int assessmentID, String assessmentType, String assessmentTitle, String endDate) {
        this.assessmentID = assessmentID;
        this.assessmentType = assessmentType;
        this.assessmentTitle = assessmentTitle;
        this.endDate = endDate;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID=" + assessmentID +
                ", assessmentType='" + assessmentType + '\'' +
                ", assessmentTitle='" + assessmentTitle + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
