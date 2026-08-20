package com.example.esm.studenttimetable.models

data class TimeTableModel(
    val SrNo: Int? = null,
    var SubjectName: String? = null,
    var StartDate: String? = null,
    var EndDate: String? = null,
    var DayName: String? = null,
    var LectureNo: String? = null,
    var SessionName: String? = null,
    var StartTime: String? = null,
    var EndTime: String? = null
)
