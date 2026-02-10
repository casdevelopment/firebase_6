package com.example.esm.welcome.models

import androidx.annotation.Keep

@Keep
data class IdentityModel(
    var UserIdentity: String? = null,

    var MobileCode: Int? = null,

    var Month: Int? = null,

    var NotificationTypeId: Int? = null,

    var StudentId: Int? = null,

    var Year: Int? = null,

    var DiaryDate: String? = null,

    var DiaryType: Int? = null
)
