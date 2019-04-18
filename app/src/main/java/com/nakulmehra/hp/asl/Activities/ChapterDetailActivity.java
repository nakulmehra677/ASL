package com.nakulmehra.hp.asl.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nakulmehra.hp.asl.Interfaces.OnChapterChangedListener;
import com.nakulmehra.hp.asl.Managers.ChapterManager;
import com.nakulmehra.hp.asl.Models.ChapterDetail;
import com.nakulmehra.hp.asl.Models.ChapterExamples;
import com.nakulmehra.hp.asl.R;

import java.util.List;

public class ChapterDetailActivity extends BaseActictivity {

    private ChapterManager chapterManager;

    private TextView textViewTitle, textViewIdentification, textViewExplanation;
    private ProgressBar progressBar;
    LinearLayout ruleLayout, exampleLayout;

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

    /////////////   Code for menu   ////////////////////////////////////////////////////////////////

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
