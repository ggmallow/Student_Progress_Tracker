package com.example.studenttracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class Terms extends AppCompatActivity implements TermAdapter.OnTermListener{
    public ArrayList<Term> allTermsList;
    public RecyclerView allTermsRecycler;

    public Term selectedTerm;
    public Integer previouslySelected = -1;
    public TermAdapter termAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        ActionBar actionBar = getSupportActionBar(); // Can probably get rid of this.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Setting up recyclerView
        allTermsList = new ArrayList<Term>();
        Repository repo = new Repository(getApplication());
        allTermsList.addAll(repo.getAllTerms());
        termAdapter = new TermAdapter(allTermsList, this);
        allTermsRecycler = findViewById(R.id.allTermsRecycler);
        RecyclerView.LayoutManager termLayout = new LinearLayoutManager(getApplicationContext());
        allTermsRecycler.setLayoutManager(termLayout);
        allTermsRecycler.setItemAnimator(new DefaultItemAnimator());
        allTermsRecycler.setAdapter(termAdapter);



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

    @Override
    public void onTermClick(int position) {

    }
}

