package com.nakulmehra.hp.asl.Models;

public class ChapterListItem {
    private String name, identification;

    public ChapterListItem() {
    }

    public ChapterListItem(String name, String identification) {
        this.name = name;
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public String getIdentification() {
        return identification;
    }
}