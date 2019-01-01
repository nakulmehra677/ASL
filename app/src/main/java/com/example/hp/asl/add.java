package com.example.hp.asl;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class add extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;

    private Button ruleAddButton, exampleAddButton;
    LinearLayout layout1, layout2, parent;
    private int editTextEnglishExampleId = 700, editTextHindiExampleId = 600, linearLayoutRuleId = 100, linearLayoutExampleId = 500;
    private Button upload;
    private EditText chapter_name, identification, explanation;

    private List<post_add_example> exampleText = new ArrayList<>();
    private List<String> ruleText = new ArrayList<>();
    private boolean empty =false;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);

        chapter_name = findViewById(R.id.chapter_name);
        identification = findViewById(R.id.identification);
        explanation = findViewById(R.id.explanation);
        ruleAddButton = findViewById(R.id.ruleAddButton);
        exampleAddButton = findViewById(R.id.exampleAddButton);
        layout1 = findViewById(R.id.Rule);
        layout2 = findViewById(R.id.Example);
        upload = findViewById(R.id.upload);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        ruleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (linearLayoutRuleId < 120 && linearLayoutRuleId >= 100) {

                    View view = LayoutInflater.from(add.this).inflate(R.layout.add_rules_layout, null);
                    view.setTag(linearLayoutRuleId);
                    layout1.addView(view);

                    EditText rule = view.findViewById(R.id.rule);
                    rule.setSingleLine(true);
                    Button removeRuleButton = view.findViewById(R.id.remove_rule_button);

                    removeRuleButton.setTag(linearLayoutRuleId);
                    rule.setId(linearLayoutRuleId);

                    removeRuleButton.setOnClickListener(add.this);

                    linearLayoutRuleId++;

                } else {
                    Toast.makeText(add.this, "You have reached maximum limit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exampleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (linearLayoutExampleId >= 500 && linearLayoutExampleId < 520) {

                    View view = LayoutInflater.from(add.this).inflate(R.layout.add_examples_layout, null);
                    view.setTag(linearLayoutExampleId);
                    layout2.addView(view);

                    EditText hindiExample = view.findViewById(R.id.hindi_example);
                    EditText englishExample = view.findViewById(R.id.english_example);
                    Button removeExampleButton = view.findViewById(R.id.remove_example_button);

                    hindiExample.setSingleLine(true);
                    englishExample.setSingleLine(true);

                    removeExampleButton.setTag(linearLayoutExampleId);
                    hindiExample.setId(editTextHindiExampleId);
                    englishExample.setId(editTextEnglishExampleId);

                    removeExampleButton.setOnClickListener(add.this);

                    linearLayoutExampleId++;
                    editTextHindiExampleId++;
                    editTextEnglishExampleId++;

                } else {
                    Toast.makeText(add.this, "You have reached maximum limit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String str_chapter_name = chapter_name.getText().toString();
                String str_identification = identification.getText().toString();
                String str_explanation = explanation.getText().toString();

                checkEmpty(str_chapter_name);
                checkEmpty(str_identification);
                checkEmpty(str_explanation);

                getRules();
                getExamples();

                if(!empty) {
                    progressBar.setVisibility(View.VISIBLE);

                    postAddDetails postAddDetails = new postAddDetails(str_chapter_name, str_identification, str_explanation);

                    databaseReference.child(str_chapter_name).setValue(postAddDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (!task.isSuccessful()) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(add.this, "Error while uploading.", Toast.LENGTH_SHORT).show();
                            } else {

                                databaseReference.child(str_chapter_name).child("rules").setValue(ruleText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (!task.isSuccessful()) {

                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(add.this, "Error while uploading.", Toast.LENGTH_SHORT).show();
                                        } else {

                                            databaseReference.child(str_chapter_name).child("examples").setValue(exampleText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(add.this, "Chapter uploaded.", Toast.LENGTH_SHORT).show();
                                                        add.super.onBackPressed();
                                                    } else {

                                                        Toast.makeText(add.this, "Error while uploading.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(add.this, "Fill all the fields to upload.", Toast.LENGTH_SHORT).show();
                    empty = false;
                }
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Your all data will be erased. Click BACK again to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        LinearLayout v2 = (LinearLayout) v.getParent();
        int id = (int) v.getTag();
        if (id >= 100 && id < 120) {
            linearLayoutRuleId--;
        } else if (id >= 500 && id < 520) {
            linearLayoutExampleId--;
            editTextHindiExampleId--;
            editTextEnglishExampleId--;
        }

        v2.setVisibility(View.GONE);
    }

    void getRules() {

        for (int i = 100; i < linearLayoutRuleId; i++) {

            EditText editText = findViewById(i);
            String s = editText.getText().toString();

            checkEmpty(s);

            if (!empty) {
                ruleText.add(s);
            }
        }
    }

    void getExamples() {

        for (int i = 500; i < linearLayoutExampleId; i++) {

            EditText editText1 = findViewById(i + 100);
            EditText editText2 = findViewById(i + 200);

            String s1 = editText1.getText().toString();
            checkEmpty(s1);

            String s2 = editText2.getText().toString();
            checkEmpty(s2);

            if (!empty) {
                post_add_example post_add_example = new post_add_example(s1, s2);
                exampleText.add(post_add_example);
            }
        }
    }

    void checkEmpty(String s) {
        if (TextUtils.isEmpty(s)) {
            empty = true;
        }
    }
}

class postAddDetails {

    private String chapter_name, identification, explanation;

    public postAddDetails(String chapter_name, String identification, String explanation) {
        this.chapter_name = chapter_name;
        this.identification = identification;
        this.explanation = explanation;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}


class post_add_example {
    private String hindi, english;

    public post_add_example(String hindi, String english) {
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


