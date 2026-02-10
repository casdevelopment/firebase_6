package com.example.esm.signup.model

data class SignUpRequestResponseModel(
    var MobileNumber: String? = null,

    var Password: String? = null,

    var DeviceToken: String?= null,

    var MobileCode: Int?= null,

    var isValid: Int?= null,

    var UserIdentity: String?= null,

    var ResponseString: String?= null
)
