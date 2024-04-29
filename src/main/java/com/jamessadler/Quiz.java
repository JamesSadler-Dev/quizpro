package com.jamessadler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Quiz {
    private final HashMap<String, ArrayList<String>> qAnswersAndExpected;
    private boolean caseInsensitive;
    private int longestQuestion;
    private final int OPT_EXPECTEDANSWER_INDEX;
    private final int QUESTION_INDEX;
    private final int USERANSWER_INDEX;
    private final ArrayList<String> questionChoices;
    private String title;
    private String suppliedQuestion;

    Quiz(boolean caseInsensitive, HashMap<String, String> questionsAnswers,String title) {
        this(questionsAnswers,title);
        this.caseInsensitive = caseInsensitive;
    }

    protected Quiz(HashMap<String, String> questionsAnswers,String title) {
        this.qAnswersAndExpected = new HashMap();
        this.caseInsensitive = true;
        this.longestQuestion = 0;
        this.OPT_EXPECTEDANSWER_INDEX = 1;
        this.QUESTION_INDEX = 0;
        this.USERANSWER_INDEX = 2;
        this.questionChoices = new ArrayList();
        setTitle(title);
        Iterator var2 = questionsAnswers.keySet().iterator();

        while(var2.hasNext()) {
            String question = (String)var2.next();
            this.addQuestionTo((String)questionsAnswers.get(question), question);
            int length = question.length();
            if (length > this.longestQuestion) {
                this.longestQuestion = length;
            }
        }

        this.defaultOptionalAnswers();
    }

    protected void addOptionalAnswer(String answer, String optionalAnswer) {
        ((ArrayList)this.qAnswersAndExpected.get(answer)).set(1, optionalAnswer);
    }

    protected void supplyBaseQuestion(String suppliedQuestion) {
        this.suppliedQuestion = suppliedQuestion;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    private void addQuestionTo(String answer, String question) {
        this.questionChoices.add(answer);
        this.qAnswersAndExpected.put(answer, new ArrayList());
        ((ArrayList)this.qAnswersAndExpected.get(answer)).add(0, question);
    }

    private void defaultOptionalAnswers() {
        Iterator var1 = this.qAnswersAndExpected.keySet().iterator();

        while(var1.hasNext()) {
            String key = (String)var1.next();
            ((ArrayList)this.qAnswersAndExpected.get(key)).add(1, key);
        }

    }

    private void askQ(String question, Scanner answers, String choice, int noAnswered) {
        StringBuilder fullQuestion = new StringBuilder();
        if (noAnswered + 1 < 10) {
            fullQuestion.append(noAnswered + 1);
            fullQuestion.append(".  ");
        } else {
            fullQuestion.append(noAnswered + 1);
            fullQuestion.append(". ");
        }

        StringBuilder questionBuilder = new StringBuilder(question);
        int remainderQuestion = this.longestQuestion - question.length();
        fullQuestion.append(questionBuilder);
        fullQuestion.append("?      ");
        questionBuilder.append(" ".repeat(Math.max(0, remainderQuestion + 1)));
        fullQuestion.append("      Your answer: ");
        System.out.print(fullQuestion);
        int optionalAnswerLength = ((String)((ArrayList)this.qAnswersAndExpected.get(choice)).get(1)).length();
        int maxAnswerLength = Math.max(choice.length(), optionalAnswerLength);

        String userAnswer;
        for(userAnswer = answers.nextLine(); userAnswer.trim().length() < 1; userAnswer = answers.nextLine()) {
            System.out.print("\ud83d\udeabMin length 1\ud83d\udeab  " + fullQuestion);
        }

        while(userAnswer.trim().length() > maxAnswerLength) {
            PrintStream var10000 = System.out;
            String var10001 = String.format("\ud83d\udeabMax length %d\ud83d\udeab  ", maxAnswerLength);
            var10000.print(var10001 + fullQuestion);
            userAnswer = answers.nextLine();
        }

        ((ArrayList)this.qAnswersAndExpected.get(choice)).add(2, userAnswer);
    }

    public double start() {
        Random randomChoice = new Random();
        Scanner usersAnswers = new Scanner(System.in);
        ArrayList<String> questionsAnswered = new ArrayList();
        int answered = 0;
        System.out.println(this.title);

        while(answered < this.questionChoices.size()) {
            String choice = (String)this.questionChoices.get(randomChoice.nextInt(this.questionChoices.size()));
            if (!questionsAnswered.contains(choice)) {
                questionsAnswered.add(choice);
                String question = (String)((ArrayList)this.qAnswersAndExpected.get(choice)).get(0);
                this.askQ(question, usersAnswers, choice, answered);
                ++answered;
            }
        }

        return this.results();
    }

    private double results() {
        double correct = 0.0;
        double incorrect = 0.0;
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println(this.title + " - Expected Answers:");
        System.out.println();
        Set<String> expectedAnswers = this.qAnswersAndExpected.keySet();
        int minQuestionFormattedLength = 45;
        int minExpectedansFormattedLength = 18;
        int minUseranswerFormattedLength = 18;
        Iterator var9 = expectedAnswers.iterator();

        String expectedAnswerUnprocessed;
        String userAnswer;
        while(var9.hasNext()) {
            expectedAnswerUnprocessed = (String)var9.next();
            expectedAnswerUnprocessed = expectedAnswerUnprocessed.trim();
            String questionUnprocessed = (String)((ArrayList)this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(0);
            userAnswer = (String)((ArrayList)this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(2);
            if (expectedAnswerUnprocessed.length() > minExpectedansFormattedLength) {
                minExpectedansFormattedLength = expectedAnswerUnprocessed.length() + 1;
            }

            if (questionUnprocessed.length() > minQuestionFormattedLength) {
                minQuestionFormattedLength = questionUnprocessed.length() + 1;
            }

            if (userAnswer.length() > minUseranswerFormattedLength) {
                minUseranswerFormattedLength = userAnswer.length() + 1;
            }
        }

        var9 = expectedAnswers.iterator();

        while(true) {
            while(var9.hasNext()) {
                expectedAnswerUnprocessed = (String)var9.next();
                StringBuilder expectedAnswerBuilder = new StringBuilder(expectedAnswerUnprocessed);
                StringBuilder userAnswerBuilder = new StringBuilder((String)((ArrayList)this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(2));
                userAnswer = (String)((ArrayList)this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(0);
                StringBuilder questionBuilder = new StringBuilder(userAnswer);
                StringBuilder answerStringFinal = new StringBuilder();
                String optionalAnswer = (String)((ArrayList)this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(1);
                if (optionalAnswer == null) {
                    optionalAnswer = expectedAnswerUnprocessed;
                }

                String expectedAnswer;
                String answer;
                if (this.caseInsensitive) {
                    expectedAnswer = expectedAnswerUnprocessed.toUpperCase().trim();
                    answer = userAnswerBuilder.toString().toUpperCase().trim();
                    optionalAnswer = optionalAnswer.toUpperCase().trim();
                } else {
                    expectedAnswer = expectedAnswerUnprocessed.trim();
                    answer = userAnswerBuilder.toString();
                    optionalAnswer = optionalAnswer.trim();
                }

                int remainderQuestion = minQuestionFormattedLength - questionBuilder.length();
                int remainderExpected = minExpectedansFormattedLength - expectedAnswerBuilder.length();
                int remainderUserAnswer = minUseranswerFormattedLength - userAnswerBuilder.length();
                questionBuilder.append(" ".repeat(Math.max(0, remainderQuestion)));
                expectedAnswerBuilder.append(" ".repeat(Math.max(0, remainderExpected)));
                userAnswerBuilder.append(" ".repeat(Math.max(0, remainderUserAnswer)));
                answerStringFinal.append(questionBuilder);
                answerStringFinal.append("|   Expected: ");
                answerStringFinal.append(expectedAnswerBuilder);
                answerStringFinal.append("|   Answered : ");
                answerStringFinal.append(userAnswerBuilder);
                if (!answer.contains(expectedAnswer) && !answer.contains(optionalAnswer)) {
                    answerStringFinal.append("|  ❌ Incorrect");
                    ++incorrect;
                    System.out.println(answerStringFinal);
                } else {
                    answerStringFinal.append("|  ✅ Correct!");
                    ++correct;
                    System.out.println(answerStringFinal);
                }
            }

            double result = correct / (correct + incorrect) * 100.0;
            System.out.println();
            System.out.printf("Your score: %.0f%s \n", result, "%");
            System.out.println();
            return result;
        }
    }
}
