package com.example.studenttracker.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "instructor") // This may not be right as this is part of the course.
public class Instructor {
    @PrimaryKey(autoGenerate = true)
    private String name;
    private int phone;
    private String email;

    public Instructor(/*int courseID, String title, String startDate, String endDate,String status, */ String name, int phone, String email) {
      //  super(courseID, title, startDate, endDate, status); needed when extending Courses, I don't believe I need that.
        this.name = name;
        this.phone = phone;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {

       return name;
       /*

        return "Instructor{" +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                '}'; */
    }
}
