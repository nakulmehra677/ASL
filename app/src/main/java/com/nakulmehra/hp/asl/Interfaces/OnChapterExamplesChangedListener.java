package com.nakulmehra.hp.asl.Interfaces;

import com.nakulmehra.hp.asl.Models.ChapterExamples;

import java.util.List;

public interface OnChapterExamplesChangedListener<T> {
    void onChapterExampleChanged(List<ChapterExamples> list);
}
