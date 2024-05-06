<h1>Quizpro- Quiz Objects for Java</h1>

Requires Maven build system

<b>Features:</b>
<ul>
<li>Matching system to accept more correct answers.</li>
<li>Optional case sensitivity feature.</li>
<li>Ability to add optional answers to also be accepted.</li>
</ul>

<hr>
Public API:<br>
<ul>
<li>
    <b>Constructors</b>: pass in Hashmap<String,String> of questions and answers for your quiz as well as a title,
    and optionally set your quiz to be case sensitive.<br><br>

        HashMap<String,String> myQsAndAnswers = new HashMap<>();
        myQsAndAnswers.put("What is my favorite color","Red");
        boolean caseInsensitivity = False;
        Quiz coolQuiz = Quiz(myQsAndAnswers,"coolest quiz");
        Quiz hardQuiz = Quiz(caseInsensitivity,topologyQandAnswers,"A Hard Quiz",);

Build up your questions and answers HashMap programmatically then make a quiz.<br>
<b>Errors thrown:</b> If Questions hashmap is null, empty, or a question/answer therein is empty an 
<b>IllegalArgumentException</b> will be thrown.
</li>
<hr>
<br>
<li>
    <b>addOptionalAnswer</b> instance method: Add an acceptable alternative answer to a question.<br>

        coolQuiz.addoptionalAnswer("Red","Rojo"); 
  It's optional to add alternative answers, but they'll increase accuracy so their use is encouraged.
</li>
<hr>
<br>
<li>
    <b>isFinished </b>instance method: Check if the quiz has been completed

        if (coolQuiz.getFinished()){
            hardQuiz.start()        //if one quiz has finished, do something else
        }
</li>
<hr>
<br>
<li>
    <b>Start </b> instance method: Start the quiz! This method returns the resulting score

        int myScore = coolQuiz.start();
</li>
</ul>
<hr>

To do list: add project to maven repository index
