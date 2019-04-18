package com.nakulmehra.hp.asl.Interfaces;

import com.nakulmehra.hp.asl.Models.ChapterListItem;

import java.util.List;

public interface OnChapterListChangedListener<T> {

    public void onChapterListChanged(List<ChapterListItem> list, String lastChapterName);
}
