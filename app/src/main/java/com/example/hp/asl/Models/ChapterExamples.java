package com.example.hp.asl.Models;

public class ChapterExamples {
    private String hindi, english;

    public ChapterExamples() {
    }

    public ChapterExamples(String hindi, String english) {
        this.hindi = hindi;
        this.english = english;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
