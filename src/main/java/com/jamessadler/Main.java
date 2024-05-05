package com.jamessadler;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        HashMap<String,String> questionsanswers = new HashMap<>();
        //questionsanswers.put(" ","pizza");
        questionsanswers.put("who's the coolest coder","james");
        questionsanswers.put("here is a really long questionnnnnnnnnnnnnn ","oops");
        Quiz testquiz = new Quiz(questionsanswers,"Coolest Quiz");
        testquiz.addOptionalAnswer("pizza","pineapple");
        testquiz.start();
        if (testquiz.getFinished()){
            int result = testquiz.getResult();
        }
    }}
