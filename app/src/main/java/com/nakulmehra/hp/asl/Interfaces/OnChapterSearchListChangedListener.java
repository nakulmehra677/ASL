package com.nakulmehra.hp.asl.Interfaces;

import com.nakulmehra.hp.asl.Models.ChapterListItem;

import java.util.List;

public interface OnChapterSearchListChangedListener<T> {
    public void onChapterSearchListChanged(List<ChapterListItem> list);

}
