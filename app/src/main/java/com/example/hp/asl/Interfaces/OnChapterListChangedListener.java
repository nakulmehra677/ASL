package com.example.hp.asl.Interfaces;

import com.example.hp.asl.Models.ChapterListItem;

import java.util.List;

public interface OnChapterListChangedListener<T> {

    public void onChapterListChanged(List<ChapterListItem> list);
}
