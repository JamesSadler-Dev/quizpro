package com.jamessadler;

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
    private final int QUESTION_INDEX = 0;
    private final int OPT_ANSWER_INDEX = 1;
    private final int USERANSWER_INDEX = 2;
    private final ArrayList<String> questionChoices;
    private String title;
    private boolean isFinished = false;
    private int result;

    public boolean getFinished(){
        return this.isFinished;
    }

    public int getResult() {
        if (this.isFinished)
            return this.result;
        return -1;
    }
    public void setTitle(String title) {
        if (!title.trim().isEmpty()) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("title length length must be greater than 0");
        }
    }

    public void addOptionalAnswer(String answer, String optionalAnswer) {
        this.qAnswersAndExpected.get(answer).set(OPT_ANSWER_INDEX, optionalAnswer);
    }

    public Quiz(HashMap<String, String> questionsAnswers,String title) {

        if (questionsAnswers == null) {
            throw new IllegalArgumentException("Quiz questions hashmap must not be null");
        }
        if (questionsAnswers.isEmpty()){
            throw new IllegalArgumentException("Quiz questions hashmap must not be empty");
        }

        for (String key: questionsAnswers.keySet()){
            if (key.trim().isEmpty()){
                throw new IllegalArgumentException("Question KEY cannot be empty");
            } else if (questionsAnswers.get(key).trim().isEmpty()){
                throw new IllegalArgumentException("Question VALUE must not be empty");
            }
        }
        this.qAnswersAndExpected = new HashMap<>();
        this.caseInsensitive = true;
        this.longestQuestion = 0;
        this.questionChoices = new ArrayList<>();
        setTitle(title);
        Iterator<String> questions = questionsAnswers.keySet().iterator();

        while(questions.hasNext()) {
            String question = (String)questions.next();
            this.addQuestionTo(questionsAnswers.get(question), question);
            int length = question.length();
            if (length > this.longestQuestion) {
                this.longestQuestion = length;
            }
        }

        this.defaultOptionalAnswers();
    }

    public Quiz(boolean caseInsensitive, HashMap<String, String> questionsAnswers,String title) {
        this(questionsAnswers,title);
        this.caseInsensitive = caseInsensitive;
    }

    private void addQuestionTo(String answer, String question) {
        this.questionChoices.add(answer);
        this.qAnswersAndExpected.put(answer, new ArrayList<>());
        this.qAnswersAndExpected.get(answer).add(QUESTION_INDEX, question);
    }

    private void defaultOptionalAnswers() {
        for (String key : this.qAnswersAndExpected.keySet())
            this.qAnswersAndExpected.get(key).add(OPT_ANSWER_INDEX, key);
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


        int remainderQuestion = this.longestQuestion - question.length();
        fullQuestion.append(question);
        fullQuestion.append("?      ");
        fullQuestion.append(" ".repeat(Math.max(0, remainderQuestion + 1)));
        fullQuestion.append("Your answer: ");
        System.out.print(fullQuestion);
        int optionalAnswerLength = ((this.qAnswersAndExpected.get(choice)).get(OPT_ANSWER_INDEX)).length();
        int maxAnswerLength = Math.max(choice.length(), optionalAnswerLength);

        String userAnswer;
        for(userAnswer = answers.nextLine(); userAnswer.trim().isEmpty(); userAnswer = answers.nextLine())
            System.out.print("\ud83d\udeabMin length 1\ud83d\udeab  " + fullQuestion);

        while(userAnswer.trim().length() > maxAnswerLength) {
            String maxLength = String.format("\ud83d\udeabMax length %d\ud83d\udeab  ", maxAnswerLength);
            System.out.print(maxLength + fullQuestion);
            userAnswer = answers.nextLine();
        }

        this.qAnswersAndExpected.get(choice).add(USERANSWER_INDEX, userAnswer);
    }

    public int start() {
        Random randomChoice = new Random();
        Scanner usersAnswers = new Scanner(System.in);
        ArrayList<String> questionsAnswered = new ArrayList<>();
        int answered = 0;
        System.out.println(this.title);

        while(answered < this.questionChoices.size()) {
            String choice = this.questionChoices.get(randomChoice.nextInt(this.questionChoices.size()));
            if (!questionsAnswered.contains(choice)) {
                questionsAnswered.add(choice);
                String question = (this.qAnswersAndExpected.get(choice)).get(QUESTION_INDEX);
                this.askQ(question, usersAnswers, choice, answered);
                ++answered;
            }
        }
        this.isFinished = true;
        return this.results();
    }

    private int results() {
        double correct = 0.0;
        double incorrect = 0.0;
        Set<String> expectedAnswers = this.qAnswersAndExpected.keySet();
        int minQuestionFormattedLength = 45;
        int minExpectedansFormattedLength = 18;
        int minUseranswerFormattedLength = 18;
        Iterator<String> answers = expectedAnswers.iterator();

        String expectedAnswerUnprocessed;
        String userAnswer;
        while(answers.hasNext()) {
            expectedAnswerUnprocessed = answers.next();
            expectedAnswerUnprocessed = expectedAnswerUnprocessed.trim();
            String questionUnprocessed = (this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(QUESTION_INDEX);
            userAnswer = (this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(USERANSWER_INDEX);

            if (expectedAnswerUnprocessed.length() > minExpectedansFormattedLength)
                minExpectedansFormattedLength = expectedAnswerUnprocessed.length() + 1;

            if (questionUnprocessed.length() > minQuestionFormattedLength)
                minQuestionFormattedLength = questionUnprocessed.length() + 1;

            if (userAnswer.length() > minUseranswerFormattedLength)
                minUseranswerFormattedLength = userAnswer.length() + 1;
        }

        answers = expectedAnswers.iterator();
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println(this.title + " - Expected Answers:");
        System.out.println();


        while(answers.hasNext()) {
            expectedAnswerUnprocessed = answers.next();
            StringBuilder expectedAnswerBuilder = new StringBuilder(expectedAnswerUnprocessed);
            StringBuilder userAnswerBuilder = new StringBuilder((this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(USERANSWER_INDEX));
            userAnswer = (this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(QUESTION_INDEX);
            StringBuilder questionBuilder = new StringBuilder(userAnswer);
            StringBuilder answerStringFinal = new StringBuilder();
            String optionalAnswer = (this.qAnswersAndExpected.get(expectedAnswerUnprocessed)).get(OPT_ANSWER_INDEX);
            String expectedAnswer;
            String answer;

            if (optionalAnswer == null)
                optionalAnswer = expectedAnswerUnprocessed;

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
                incorrect++;
                System.out.println(answerStringFinal);
            } else {
                answerStringFinal.append("|  ✅ Correct!");
                correct++;
                System.out.println(answerStringFinal);
            }
        }

        double result = correct / (correct + incorrect) * 100.0;
        System.out.println();
        System.out.printf("Your score: %.0f%s \n", result, "%");
        System.out.println();
        return (int) result;
    }
}

