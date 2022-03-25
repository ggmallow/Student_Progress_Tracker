package com.example.studenttracker.UI;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.studenttracker.R;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    public DrawerLayout navDrawer;
    public ActionBarDrawerToggle drawerToggle;
    public static int alertNum;



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



        nav_View.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.terms) {
                Intent intent = new Intent(MainActivity.this,Terms.class);
                navDrawer.closeDrawers();
                startActivity(intent);

            }

            if (id == R.id.courses) {
                Intent intent = new Intent(MainActivity.this,Courses.class);
                navDrawer.closeDrawers();
                startActivity(intent);
            }

            if (id == R.id.assessments) {
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