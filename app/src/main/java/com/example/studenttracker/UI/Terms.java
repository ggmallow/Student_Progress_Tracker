package com.example.studenttracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Assessment;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class Terms extends AppCompatActivity implements TermAdapter.OnTermListener{
    public ArrayList<Term> allTermsList  = new ArrayList<>();;
    public RecyclerView allTermsRecycler;

    public Term selectedTerm;
    public Integer previouslySelected = -1;
    public TermAdapter termAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Setting up recyclerView
        allTermsRecycler = findViewById(R.id.allTermsRecycler);
        allTermsList = new ArrayList<Term>();

        Repository repo = new Repository(getApplication());
        allTermsList.addAll(repo.getAllTerms());

        termAdapter = new TermAdapter(allTermsList, this);

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
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select an assessment.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(Terms.this,AddTerm.class);
        intent.putExtra("moddingTerm", previouslySelected);
        startActivity(intent);
    }

    public void deleteTerms(View view) {
        //Handles button click, when no assessment is selected.
        if (previouslySelected == -1) {
            Toast.makeText(this, "You must select an assessment.", Toast.LENGTH_LONG).show();
            return;
        }

        Repository repo = new Repository(getApplication());
        repo.deleteTerm(selectedTerm);
        allTermsList.clear();
        allTermsList.addAll(repo.getAllTerms());
        termAdapter.notifyItemRemoved(previouslySelected);
    }

    //Remove save button, use same form as AddTerm
    public void termDetails(View view) {
        int detailView = 1; // Used for UI, when navigating to detail view. This helps bc of bundle confusion.

        Intent intent = new Intent(Terms.this,AddTerm.class);
        intent.putExtra("termDetails", previouslySelected);
        intent.putExtra("detailView", detailView);
        startActivity(intent);
    }

    @Override
    public void onTermClick(int position) {
        Log.println(Log.INFO,"debug", "You have picked: " + allTermsList.get(position).getTitle());
        previouslySelected = position;

        //Creates an assessment based off selection. Used for delete method.
        selectedTerm = new Term(
                Integer.parseInt(String.valueOf(allTermsList.get(position).getTermID())),
                allTermsList.get(position).getTitle(),
                allTermsList.get(position).getStartDate(),
                allTermsList.get(position).getEndDate());

    }
}

