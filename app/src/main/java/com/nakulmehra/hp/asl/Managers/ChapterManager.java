package com.nakulmehra.hp.asl.Managers;

import android.content.Context;

import com.nakulmehra.hp.asl.Interfaces.OnChapterChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterExamplesChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterRulesChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterUploadListener;
import com.nakulmehra.hp.asl.Models.ChapterDetail;
import com.nakulmehra.hp.asl.Models.ChapterExamples;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChapterManager {

    private DatabaseManager databaseManager;
    private Context context;
    private String name;

    public ChapterManager(String name, Context context) {
        this.context = context;
        this.name = name;
        databaseManager = new DatabaseManager(this.context);

    }

    public void getChapterDetails(OnChapterChangedListener listener) {
        databaseManager.getChapter(this.name, listener);
    }

    public void getChapterRulesDetails(OnChapterRulesChangedListener<List<String>> listener) {
        databaseManager.getChapterRules(this.name, listener);
    }

    public void getChapterExamplesDetails(OnChapterExamplesChangedListener<List<ChapterExamples>> listener) {
        databaseManager.getChapterExamples(this.name, listener);
    }

    public void removeChapter() {
        FirebaseDatabase.getInstance().getReference().child(this.name).removeValue();
    }

    public void uploadChapterDetails(ChapterDetail chapterDetail, OnChapterUploadListener listener){
        databaseManager.uploadChapter(chapterDetail, listener);
    }
}
