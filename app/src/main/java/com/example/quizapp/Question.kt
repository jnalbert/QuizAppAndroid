package com.example.quizapp

data class Question(
    val question: String,
    val answer: String,
    val choices: List<String>
) {
    fun isCorrect(answer: String): Boolean {
        return this.answer == answer
    }
}
