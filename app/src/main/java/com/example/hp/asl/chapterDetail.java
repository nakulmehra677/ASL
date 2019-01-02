package com.example.hp.asl;

import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class chapterDetail extends AppCompatActivity {

    private TextView textViewTitle, textViewIdentification, textViewExplanation;
    private ProgressBar progressBar;
    LinearLayout ruleLayout, exampleLayout;
    broadcast broadcast = new broadcast();


    DatabaseReference databaseReference, rules_data, examples_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);

        String data = getIntent().getStringExtra("position");

        findViewById(R.id.scrollView).setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference(data);
        rules_data = databaseReference.child("rules");
        examples_data = databaseReference.child("examples");

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewIdentification = findViewById(R.id.identification);
        textViewExplanation = findViewById(R.id.expalanation);
        ruleLayout = findViewById(R.id.ruleLayout);
        exampleLayout = findViewById(R.id.exampleLayout);

        textViewTitle.setText(data);

        fetchData();

    }

    void fetchData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postDetails postDetails = dataSnapshot.getValue(com.example.hp.asl.postDetails.class);

                assert postDetails != null;
                textViewIdentification.setText(postDetails.getIdentification());
                textViewExplanation.setText(postDetails.getExplanation());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        rules_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot d :dataSnapshot.getChildren()) {

                    TextView textView = new TextView(chapterDetail.this);
                    textView.setText("\u2022 " + d.getValue(String.class));
                    ruleLayout.addView(textView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        examples_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postExamples postExamples;
                for(DataSnapshot d :dataSnapshot.getChildren()) {

                    postExamples = d.getValue(postExamples.class);

                    TextView textViewHindi = new TextView(chapterDetail.this);
                    textViewHindi.setText(postExamples.getHindi());

                    TextView textViewEnglish = new TextView(chapterDetail.this);
                    textViewEnglish.setText(postExamples.getEnglish());
                    textViewEnglish.setTypeface(textViewEnglish.getTypeface(), Typeface.BOLD);

                    LinearLayout example_Layout = new LinearLayout(chapterDetail.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10,10,10,10);
                    example_Layout.setOrientation(LinearLayout.VERTICAL);
                    example_Layout.setLayoutParams(lp);

                    example_Layout.addView(textViewHindi);
                    example_Layout.addView(textViewEnglish);
                    exampleLayout.addView(example_Layout);
                }

                progressBar.setVisibility(View.GONE);
                findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcast, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcast);
    }
}

class postDetails {

    private String identification, explanation;

    public postDetails() { }

    public postDetails(String identification, String explanation) {
        this.identification = identification;
        this.explanation = explanation;
    }

    public String getIdentification() {
        return identification;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

class postExamples {

    private String hindi, english;

    public postExamples() {}

    public postExamples(String hindi, String english) {
        this.hindi = hindi;
        this.english = english;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}


