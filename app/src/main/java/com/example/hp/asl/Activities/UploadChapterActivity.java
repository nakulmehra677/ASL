package com.example.hp.asl.Activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.hp.asl.Interfaces.OnChapterUploadListener;
import com.example.hp.asl.Managers.ChapterManager;
import com.example.hp.asl.Models.ChapterDetail;
import com.example.hp.asl.Models.ChapterExamples;
import com.example.hp.asl.R;
import com.example.hp.asl.broadcast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UploadChapterActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    ChapterManager chapterManager;

    private Button ruleAddButton, exampleAddButton;
    LinearLayout layout1, layout2;
    ConstraintLayout parentLayout;
    private Button upload;
    private EditText chapter_name, identification, explanation;

    private String str_chapter_name, str_identification, str_explanation;
    private List<ChapterExamples> exampleText = new ArrayList<>();
    private List<String> ruleText = new ArrayList<>();

    private List<Integer> ruleLayoutId = new ArrayList<>();
    private List<Integer> exampleLayoutId = new ArrayList<>();

    private boolean empty = false;

    com.example.hp.asl.broadcast broadcast = new broadcast();

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
        parentLayout = findViewById(R.id.parent_layout);
        upload = findViewById(R.id.upload);


        ruleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(UploadChapterActivity.this).inflate(R.layout.add_rules_layout, null);
                int id = View.generateViewId();
                view.setId(id);
                ruleLayoutId.add(id);
                layout1.addView(view);

                Button removeRuleButton = view.findViewById(R.id.remove_rule_button);
                removeRuleButton.setOnClickListener(UploadChapterActivity.this);
            }
        });

        exampleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(UploadChapterActivity.this).inflate(R.layout.add_examples_layout, null);
                int id = View.generateViewId();
                view.setId(id);
                exampleLayoutId.add(id);
                layout2.addView(view);

                Button removeExampleButton = view.findViewById(R.id.remove_example_button);
                removeExampleButton.setOnClickListener(UploadChapterActivity.this);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empty = preProcessDetails();

                if (!empty) {
                    parentLayout.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    ChapterDetail chapterDetail = new ChapterDetail(str_chapter_name, str_identification, str_explanation, ruleText, exampleText);

                    chapterManager = new ChapterManager(str_chapter_name, UploadChapterActivity.this);
                    chapterManager.uploadChapterDetails(chapterDetail, onChapterUploadListener());

                } else {
                    Toast.makeText(UploadChapterActivity.this, "Fill all the fields to upload.", Toast.LENGTH_SHORT).show();
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
        int id = (int) v2.getId();

        if (ruleLayoutId.contains(id))
            ruleLayoutId.remove(Integer.valueOf(id));
        else if (exampleLayoutId.contains(id))
            exampleLayoutId.remove(Integer.valueOf(id));

        v2.setVisibility(View.GONE);
    }

    private boolean preProcessDetails() {
        ruleText.clear();
        exampleText.clear();

        str_chapter_name = chapter_name.getText().toString();
        if(checkEmpty(str_chapter_name))
            return true;

        str_identification = identification.getText().toString();
        if(checkEmpty(str_identification))
            return true;

        str_explanation = explanation.getText().toString();
        if(checkEmpty(str_explanation))
            return true;

        for (int i : ruleLayoutId) {
            LinearLayout l = findViewById(i);
            EditText editText = l.findViewById(R.id.rule);

            String str_rule = editText.getText().toString();
            if(checkEmpty(str_rule))
                return true;

            ruleText.add(str_rule);
        }

        for (int i : exampleLayoutId) {
            LinearLayout l = findViewById(i);
            EditText editText1 = l.findViewById(R.id.hindi_example);
            String str_hindi_example = editText1.getText().toString();
            if(checkEmpty(str_hindi_example))
                return true;

            EditText editText2 = l.findViewById(R.id.english_example);
            String str_english_example = editText2.getText().toString();
            if(checkEmpty(str_english_example))
                return true;

            ChapterExamples chapterExamples = new ChapterExamples(str_hindi_example,str_english_example);
            exampleText.add(chapterExamples);
        }
        return false;
    }

    private boolean checkEmpty(String s) {
        if (TextUtils.isEmpty(s))
            return true;
        return false;
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

    private OnChapterUploadListener onChapterUploadListener() {
        return new OnChapterUploadListener() {
            @Override
            public void onChapterUploaded(boolean b) {
                progressBar.setVisibility(View.GONE);
                if (b) {
                    Toast.makeText(UploadChapterActivity.this, "Chapter uploaded.", Toast.LENGTH_SHORT).show();
                    UploadChapterActivity.super.onBackPressed();
                } else
                    Toast.makeText(UploadChapterActivity.this, "Error while uploading.", Toast.LENGTH_SHORT).show();
            }
        };
    }
}


