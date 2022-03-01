package com.example.studenttracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.studenttracker.R;

public class Terms extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addTerm(View view) {
        Intent intent = new Intent(Terms.this,AddTerm.class);
        startActivity(intent);
    }

    public void editTerms(View view) {
        Intent intent = new Intent(Terms.this,AddTerm.class);
        startActivity(intent);
    }

    public void deleteTerms(View view) {
    }

    //Remove save button, use same form as AddTerm
    public void termDetails(View view) {
        Intent intent = new Intent(Terms.this,AddTerm.class);
        startActivity(intent);
    }
}

