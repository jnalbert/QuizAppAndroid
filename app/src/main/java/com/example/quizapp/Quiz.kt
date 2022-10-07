package com.example.quizapp

class Quiz(
    var currentIndex: Int = 0,
    var score: Int = 0,
    var questions: List<Question>
) {

    fun getCurrentQuestion(): Question {

        return questions[currentIndex]
    }
    fun isNextQuestion(): Boolean {
        return currentIndex < questions.size
    }
    fun isAnswerCorrect(choice: String): Boolean{
        if (choice == questions[currentIndex].answer) {
            score++
            currentIndex++
            return true
        } else {
            currentIndex++
            return false
        }
    }
    // make a shuffle function
    fun shuffleQuestions() {
        questions = questions.shuffled()
    }

    fun restartQuiz() {
        currentIndex = 0
        score = 0
    }

}
