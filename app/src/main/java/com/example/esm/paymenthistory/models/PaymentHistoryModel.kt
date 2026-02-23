package com.example.esm.paymenthistory.models

data class PaymentHistoryModel(
    val FeePaymentMasterId:Int?= null,
    var Date: String?= null,
    val DepositWay: String?= null,
    var Amount: String?= null,
    var FeeDepositId: String?= null,
    var UC_SchoolId: String?= null

)

data class PaymentHistoryPostModel(
    var UserIdentity: String?= null,
    var SchoolId: String?= null,
    var StudentId: String?= null,
    var FeeDepositeId: String?= null,
)
