package com.example.esm.alertssms.models

import com.google.gson.annotations.SerializedName

data class AlertSmsModel(
    val Message: String,
    val ShortMessage: String,
    @SerializedName("SchoolName")
    val Date: String,
    val SmsMask: String

)
