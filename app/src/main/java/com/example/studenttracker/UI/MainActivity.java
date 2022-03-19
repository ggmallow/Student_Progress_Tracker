package com.example.studenttracker.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Instructor;
import com.example.studenttracker.Models.Term;
import com.example.studenttracker.R;
import com.google.android.material.navigation.NavigationView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout navDrawer;
    public ActionBarDrawerToggle drawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navDrawer = (DrawerLayout)findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, navDrawer,R.string.Open, R.string.Close);

        drawerToggle.setDrawerIndicatorEnabled(true);
        navDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final NavigationView nav_View = findViewById(R.id.nav);

/*
        //This is just test data and can be removed later.
        Repository repo = new Repository(getApplication());
        Term test = new Term(3,"Test Class", "testDate", "endTestDate");
        repo.insertTerm(test); */


        nav_View.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.terms) {
                System.out.println("Navigate to terms here.");
                Intent intent = new Intent(MainActivity.this,Terms.class);
                navDrawer.closeDrawers();
                startActivity(intent);

            }

            if (id == R.id.courses) {
                System.out.println("Navigate to courses here");
                Intent intent = new Intent(MainActivity.this,Courses.class);
                navDrawer.closeDrawers();
                startActivity(intent);
            }

            if (id == R.id.assessments) {
                System.out.println("Navigate to assessments here");
                Intent intent = new Intent(MainActivity.this,Assessments.class);
                navDrawer.closeDrawers();
                startActivity(intent);
            }

            return true;
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);


    }


}