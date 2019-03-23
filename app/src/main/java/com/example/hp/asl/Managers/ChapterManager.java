package com.example.hp.asl.Managers;

import android.content.Context;

import com.example.hp.asl.Activities.ChapterListActivity;
import com.example.hp.asl.Interfaces.OnChapterChangedListener;
import com.example.hp.asl.Interfaces.OnChapterExamplesChangedListener;
import com.example.hp.asl.Interfaces.OnChapterRulesChangedListener;
import com.example.hp.asl.Interfaces.OnChapterUploadListener;
import com.example.hp.asl.Models.ChapterDetail;
import com.example.hp.asl.Models.ChapterExamples;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
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
