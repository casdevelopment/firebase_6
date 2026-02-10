package com.example.esm.paymenthistory.models

data class PaymentHistoryModel(
    val FeePaymentMasterId:Int?= null,
    var Date: String?= null,
    val DepositWay: String?= null,
    var Amount: String?= null

)
