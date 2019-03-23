package com.example.hp.asl.Models;

import java.util.List;

public class ChapterDetail {
    private String name, identification, explanation;
    private List<String> rules;
    private List<ChapterExamples> examples;

    public ChapterDetail() { }

    public ChapterDetail(String name, String identification, String explanation, List<String> rules, List<ChapterExamples> examples) {
        this.name = name;
        this.identification = identification;
        this.explanation = explanation;
        this.rules = rules;
        this.examples = examples;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public void setExamples(List<ChapterExamples> examples) {
        this.examples = examples;
    }

    public List<String> getRules() {
        return rules;
    }

    public List<ChapterExamples> getExamples() {
        return examples;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentification() {
        return identification;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}