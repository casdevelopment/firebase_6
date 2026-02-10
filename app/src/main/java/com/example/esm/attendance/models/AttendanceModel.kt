package com.example.esm.attendance.models

data class AttendanceModel(
    val AttendanceDate : String?= null,
    var AttendanceDateString : String? = null,
    var TimeIn : String? = null,
    var TimeOut : String? = null,
    var Status : String? = null
)
