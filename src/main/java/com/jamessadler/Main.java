package com.jamessadler;

import java.util.HashMap;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        HashMap<String,String> questionsanswers = new HashMap<>();
        questionsanswers.put("what is my fav food","pizza");
        questionsanswers.put("who's the coolest coder","james");
        questionsanswers.put("really long question test something is broken","oops");
        Quiz testquiz = new Quiz(questionsanswers,"Coolest Quiz");
        testquiz.start();
        }
    }
