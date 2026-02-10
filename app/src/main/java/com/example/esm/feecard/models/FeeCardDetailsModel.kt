package com.example.esm.feecard.models

data class FeeCardDetailsModel(
    val TFeeHeadID: Int? = null,
    var TFeeHeadName: String? = null,
    var TRemaining: Long? = null,
    var TPrevBalance: Long? = null,
    var TFeeHeadActualAmount: Long? = null
)
