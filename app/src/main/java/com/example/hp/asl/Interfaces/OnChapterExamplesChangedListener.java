package com.example.hp.asl.Interfaces;

import com.example.hp.asl.Models.ChapterExamples;

import java.util.List;

public interface OnChapterExamplesChangedListener<T> {
    void onChapterExampleChanged(List<ChapterExamples> list);
}
