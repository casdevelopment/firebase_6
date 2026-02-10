package com.example.esm.complaint.models

import com.google.gson.annotations.SerializedName

data class ComplaintResponseList(
    var ComplaintIdFk:Int,
    var UserIdentity:String,
    @SerializedName("CMS_Mobile_Complaints_Response")
    var ComplaintResponse: ArrayList<ComplaintResponseModel?>? = null
)
