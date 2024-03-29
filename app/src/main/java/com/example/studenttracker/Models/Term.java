package com.example.studenttracker.Models;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private Integer termID; //Autogenerated as PrimaryKey

    private String title;
    private String startDate;
    private String endDate;

  public Term(Integer termID, String title, String startDate, String endDate) {
      this.termID = termID;
      this.title = title;
      this.startDate = startDate;
      this.endDate = endDate;

  }

    public Integer getTermID() {
        return termID;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
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

//  I have not used this before.
    @Override
    public String toString() {
        return "Term{" +
                "termId=" + termID +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
