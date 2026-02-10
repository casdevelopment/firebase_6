package com.example.esm.feecard.models

import com.google.gson.annotations.SerializedName

data class FeeCardMasterModel(
    val FeeMonthYear:  String?= null,
    val StringDueDate: String?= null,
    @SerializedName("cMS_Mobile_FeeCardDetail")
    val feeCardDetail: List<FeeCardDetailsModel>?= null

)
