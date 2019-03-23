package com.example.hp.asl.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hp.asl.Interfaces.OnChapterChangedListener;
import com.example.hp.asl.Managers.ChapterManager;
import com.example.hp.asl.Fragments.warningFragment;
import com.example.hp.asl.Models.ChapterDetail;
import com.example.hp.asl.Models.ChapterExamples;
import com.example.hp.asl.R;
import com.example.hp.asl.broadcast;

import java.util.List;

public class ChapterDetailActivity extends AppCompatActivity {

    private ChapterManager chapterManager;

    private TextView textViewTitle, textViewIdentification, textViewExplanation;
    private ProgressBar progressBar;
    LinearLayout ruleLayout, exampleLayout;
    com.example.hp.asl.broadcast broadcast = new broadcast();

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);

        name = getIntent().getStringExtra("position");

        findViewById(R.id.scrollView).setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewIdentification = findViewById(R.id.identification);
        textViewExplanation = findViewById(R.id.expalanation);
        ruleLayout = findViewById(R.id.ruleLayout);
        exampleLayout = findViewById(R.id.exampleLayout);

        textViewTitle.setText(name);

        fetchData();

    }

    private void fetchData() {
        chapterManager = new ChapterManager(name, this);
        chapterManager.getChapterDetails(onChapterChangedListener());
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

    /////////////   Code for menu   ////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chapter_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(this, UploadChapterActivity.class));
                return true;

            case R.id.delete:
                warningFragment newFragment = warningFragment.newInstance(
                        "Are you sure to delete this chapter");
                newFragment.show(getFragmentManager(), "dialog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteChapterWarningFragmentDoPositiveClick() {
        chapterManager.removeChapter();
        finish();
    }

    /////////////   Code for filling views with data   /////////////////////////////////////////////

    private OnChapterChangedListener<ChapterDetail> onChapterChangedListener() {
        return new OnChapterChangedListener<ChapterDetail>() {
            @Override
            public void onChapterChanged(ChapterDetail chapterDetail) {

                textViewIdentification.setText(chapterDetail.getIdentification());
                textViewExplanation.setText(chapterDetail.getExplanation());

                List<String> chapterRules = chapterDetail.getRules();
                for (String rule : chapterRules) {
                    TextView textView = new TextView(ChapterDetailActivity.this);
                    textView.setText("\u2022 " + rule);
                    ruleLayout.addView(textView);
                }


                List<ChapterExamples> chapterExamples = chapterDetail.getExamples();
                for (ChapterExamples example : chapterExamples) {
                    TextView textViewHindi = new TextView(ChapterDetailActivity.this);
                    textViewHindi.setText(example.getHindi());

                    TextView textViewEnglish = new TextView(ChapterDetailActivity.this);
                    textViewEnglish.setText(example.getEnglish());
                    textViewEnglish.setTypeface(textViewEnglish.getTypeface(), Typeface.BOLD);

                    LinearLayout example_Layout = new LinearLayout(ChapterDetailActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 10, 10, 10);
                    example_Layout.setOrientation(LinearLayout.VERTICAL);
                    example_Layout.setLayoutParams(lp);

                    example_Layout.addView(textViewHindi);
                    example_Layout.addView(textViewEnglish);
                    exampleLayout.addView(example_Layout);

                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                }
            }
        };
    }
}
