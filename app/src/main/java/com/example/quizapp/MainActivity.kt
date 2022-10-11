package com.example.quizapp

import android.graphics.Color
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    var scoreString = R.string.main_score.toString()
    lateinit var quiz: Quiz
    lateinit var activeQuizGroup: Group
    lateinit var scoreText: TextView
    lateinit var questionText: TextView
    lateinit var buttonOptions: List<Button>
    lateinit var finalScoreGroup: Group
    lateinit var finalScoreText: TextView
    lateinit var playAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQuestions()
        wireWidgets()

        // initial values
        scoreText.text = "${scoreString} 0/0"
        // sets the listeners for the buttons
        setListeners()
        displayNewQuestion()
    }

    fun displayNewQuestion() {
        // checks if there is a next question
        if (!quiz.isNextQuestion()) {
            endQuiz()
            return
        }
        // gets the current question and sets the text
        val question = quiz.getCurrentQuestion()
        questionText.text = question.question

        // sets the text of the used buttons
        for (i in 0 until question.choices.size) {
            Log.d(TAG, "displayNewQuestion: ${i}")
            buttonOptions[i].text = question.choices[i]
            buttonOptions[i].alpha = 1f
            buttonOptions[i].isEnabled = true
        }
        // sets the unused button to INVISIBLE
        for (i in question.choices.size until buttonOptions.size) {
            Log.d(TAG, "displayNewQuestion: hiding button $i")
            buttonOptions[i].alpha = 0f
            buttonOptions[i].isEnabled = false
        }
    }

    fun answerClicked(button: Button) {
        // checks if the answer is correct
        quiz.isAnswerCorrect(button.text.toString())
        // update the score
        scoreText.text = "${scoreString} ${quiz.score}/${quiz.currentIndex}"
        displayNewQuestion()
    }

    fun endQuiz() {
        activeQuizGroup.visibility = Group.GONE
        finalScoreGroup.visibility = Group.VISIBLE
        finalScoreText.text = "Your final score is ${quiz.score}/${quiz.currentIndex}"
        playAgainButton.setOnClickListener { resetQuiz() }
    }

    fun resetQuiz() {
        quiz.restartQuiz()
        scoreText.text = "$scoreString 0/0"
        activeQuizGroup.visibility = Group.VISIBLE
        finalScoreGroup.visibility = Group.GONE
        displayNewQuestion()
    }



    fun setListeners() {
        for (button in buttonOptions) {
            button.setOnClickListener { answerClicked(it as Button) }
        }
    }

    fun loadQuestions() {
        val inputStream = resources.openRawResource(R.raw.questions)
        // raw json file
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // convert json to list of questions
        val gson = Gson()
        val type = object : TypeToken<List<Question>>() { }.type
        val questions: List<Question> = gson.fromJson(jsonString, type)

        quiz = Quiz(0, 0, questions)
        Log.d(TAG, "loadQuestions: ${quiz.questions}")
//        quiz.shuffleQuestions()
    }

    fun wireWidgets() {
        activeQuizGroup = findViewById(R.id.group_main_activeQuiz)
        scoreText = findViewById(R.id.textView_main_score)
        questionText = findViewById(R.id.textView_main_question)
        buttonOptions = List(4) { index ->
            findViewById<Button?>(
                resources.getIdentifier(
                    "button_main_option${index + 1}",
                    "id",
                    packageName
                )
            )
        }
        finalScoreGroup = findViewById(R.id.group_main_finalScore)
        finalScoreText = findViewById(R.id.textView_main_finalScore)
        playAgainButton = findViewById(R.id.button_main_playAgain)
    }
}