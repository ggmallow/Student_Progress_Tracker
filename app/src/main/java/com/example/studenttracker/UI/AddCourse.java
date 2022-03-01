package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.studenttracker.R;

import java.lang.reflect.Array;

public class AddCourse extends AppCompatActivity {
    public TextView startDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        startDate = findViewById(R.id.startDate);
        startDate.setText("Pick a start date");
    }

}
