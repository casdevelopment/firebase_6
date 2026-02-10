package com.example.esm.results.models

data class ResultDetailsModel(
    val Subject: String,
    val EvaluationDate: String,
    val EvaluationDateString: String,
    val TotalMarks: Int,
    val ObtainedMarks: Float,
    val Percentage: Float ,
    val Grade: String
)
