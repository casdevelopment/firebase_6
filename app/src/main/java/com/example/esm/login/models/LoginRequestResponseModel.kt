package com.example.esm.login.models


data class LoginRequestResponseModel(
    var FcmToken: String? = null,
    var MessageCode: Int? = null,
    var MobileCode: Int? = null,
    var MobileNumber: String? = null,
    var Password: String? = null,
    var isValid: Int? = null
)
