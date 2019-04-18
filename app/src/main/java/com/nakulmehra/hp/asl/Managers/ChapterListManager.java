package com.nakulmehra.hp.asl.Managers;

import android.content.Context;

import com.nakulmehra.hp.asl.Interfaces.OnChapterListChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterSearchListChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnLastChapterChangedListener;
import com.nakulmehra.hp.asl.Models.ChapterListItem;

import java.util.List;

public class ChapterListManager {
    private Context context;
    private DatabaseManager databaseManager;

    public ChapterListManager(Context context) {
        this.context = context;
        databaseManager = new DatabaseManager(this.context);
    }

    public void getChapterListDetails(OnChapterListChangedListener<List<ChapterListItem>> listener, String lastChapter) {
        databaseManager.getChapterList(listener, lastChapter);
    }

    public void getLastChapterDetails(OnLastChapterChangedListener onLastChapterChangedListener) {
        databaseManager.getLastChapter(onLastChapterChangedListener);
    }

    public void searchChapter(OnChapterSearchListChangedListener<List<ChapterListItem>> listener, String search){
        databaseManager.search(listener, search);

    }
}
