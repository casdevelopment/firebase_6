package com.example.esm.complaint.models

data class ComplaintModel(
    var ComplaintId: Int?= null,

    var ComplaintTitle: String?= null,

    var ComplaintText: String?= null,

    var ComplaintStatus: String?= null,

    var Logged: Int?= null,

    var UserIdentity: String?= null,

    var StudentId: String?= null
)

