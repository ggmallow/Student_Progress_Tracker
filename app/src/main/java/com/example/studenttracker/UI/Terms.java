package com.example.studenttracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttracker.Database.Repository;
import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;
import com.example.studenttracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Terms extends AppCompatActivity implements TermAdapter.OnTermListener{
    public ArrayList<Term> allTermsList  = new ArrayList<>();;
    public RecyclerView allTermsRecycler;

    public TextView termsLabel;
    public Term selectedTerm;
    public TermAdapter termAdapter;
    public FloatingActionButton deleteTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        termsLabel = findViewById(R.id.termsLabel);

        //Setting up recyclerView
        allTermsRecycler = findViewById(R.id.allTermsRecycler);
        deleteTerms = findViewById(R.id.deleteTerms);

        allTermsList = new ArrayList<Term>();
        Handler handler = new Handler();
        termsLabel.setText("Loading...");
        loadTermData(new LoadTermData() {
            @Override
            public void onComplete(List<Term> terms) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        termsLabel.setText("Terms");
                        allTermsList.addAll(terms);
                        termAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        termAdapter = new TermAdapter(allTermsList, this);

        RecyclerView.LayoutManager termLayout = new LinearLayoutManager(getApplicationContext());
        allTermsRecycler.setLayoutManager(termLayout);
        allTermsRecycler.setItemAnimator(new DefaultItemAnimator());
        allTermsRecycler.setAdapter(termAdapter);

        initDeleteTerm();

    }

    interface LoadTermData {
        void onComplete(List<Term> terms);
    }

    private void loadTermData(LoadTermData callBack) {


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Repository repo = new Repository(getApplication());
                List<Term> allTerms = repo.getAllTerms();
                callBack.onComplete(allTerms);
            }
        });
    }

    private void initDeleteTerm() {

        deleteTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termAdapter.checkedPosition == -1) {
                    Toast.makeText(Terms.this, "You must select a term.", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Terms.this);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setTitle("You are about to delete: " + selectedTerm.getTitle());
                alertDialogBuilder.setMessage("By clicking OK, all Courses attached to this Term will also be deleted.");

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(Terms.this, "You have decided against your action.", Toast.LENGTH_LONG).show();

                    }
                });

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Repository repo = new Repository(getApplication());
                        ArrayList<Course> coursesToDelete = new ArrayList<Course>();
                        coursesToDelete.addAll(repo.getAllCourses());


                        for (Course toDelete : coursesToDelete) {
                            if (toDelete.getTermID() == selectedTerm.getTermID()) {
                                repo.deleteCourse(toDelete);
                            }
                        }
                        int selectedTermIndex = allTermsList.indexOf(selectedTerm);

                        repo.deleteTerm(selectedTerm);
                        allTermsList.remove(selectedTerm);
                        termAdapter.notifyItemRemoved(selectedTermIndex);
                        termAdapter.checkedPosition = -1;

                    }
                });
                alertDialogBuilder.show();

            }
        });


    }

    public void addTerm(View view) {
        Intent intent = new Intent(Terms.this,AddTerm.class);
        startActivity(intent);
    }

    public void editTerms(View view) {
        if (selectedTerm == null) {
            Toast.makeText(this, "You must select a Term.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(Terms.this,AddTerm.class);
        intent.putExtra("termID", selectedTerm.getTermID());
        startActivity(intent);
    }

   /* public void deleteTerms(View view) {
        //Handles button click, when no assessment is selected.
        if (selectedTerm == null) {
            Toast.makeText(this, "You must select a Term.", Toast.LENGTH_LONG).show();
            return;
        }

        Repository repo = new Repository(getApplication());
        repo.deleteTerm(selectedTerm);
        allTermsList.clear();
        allTermsList.addAll(repo.getAllTerms());
        termAdapter.notifyItemRemoved(previouslySelected);
    }
*/
    public void termDetails(View view) {
        int detailView = 1; // Used for UI, when navigating to detail view. This helps bc of bundle confusion.

        Intent intent = new Intent(Terms.this,AddTerm.class);
        intent.putExtra("termID", selectedTerm.getTermID());
        intent.putExtra("detailView", detailView);
        startActivity(intent);
    }

    @Override
    public void onTermClick(int position) {
        selectedTerm = allTermsList.get(position);

    }
}

