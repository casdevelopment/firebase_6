package com.example.esm.complaint.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComplaintResponseModel(
    val Id:Int,
    val ComplaintIdFk:Int,
    val Response: String,
    val ComplaintResponseType:String,
    val UserIdentity:String,
    val Logged: Int
) : Parcelable