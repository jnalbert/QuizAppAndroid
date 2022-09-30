package com.example.quizapp

data class Quiz(
    var currentIndex: Int,
    var score: Int,
    var questions: List<Question>
) {
    fun getNextQuestion(): Question {
        currentIndex++
        return questions[currentIndex]
    }
    fun isNextQuestion(): Boolean {
        return currentIndex < questions.size
    }
    fun isAnswerCorrect(choice: String): Boolean{
        if (choice == questions[currentIndex].answer) {
            score++
            return true
        }
        return false;
    }
}
