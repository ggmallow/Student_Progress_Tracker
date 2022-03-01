package com.example.studenttracker.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "instructor") // This may not be right as this is part of the course.
public class Instructor extends Course {
    @PrimaryKey(autoGenerate = true)
    private String name;
    private int phone;
    private String email;

    public Instructor(String title, LocalDateTime startDate, LocalDateTime endDate,String status, String name, int phone, String email) {
        super(title, startDate, endDate, status);
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
}
