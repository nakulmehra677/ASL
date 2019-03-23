package com.example.hp.asl.Managers;

import android.content.Context;

import com.example.hp.asl.Interfaces.OnChapterListChangedListener;
import com.example.hp.asl.Models.ChapterListItem;

import java.util.List;

public class ChapterListManager {
    private Context context;
    private DatabaseManager databaseManager;

    public ChapterListManager(Context context) {
        this.context = context;
        databaseManager = new DatabaseManager(this.context);
    }

    public void getChapterListDetails(OnChapterListChangedListener<List<ChapterListItem>> listener) {
        databaseManager.getChapterList(listener);
    }
}
