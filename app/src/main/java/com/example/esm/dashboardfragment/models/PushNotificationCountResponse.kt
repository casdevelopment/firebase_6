package com.example.esm.dashboardfragment.models

/*data class PushNotificationCountResponse(
    val getPushNotificationCountData : List<GetPushNotificationCountData>?=null,

)*/
typealias PushNotificationCountResponse = ArrayList<GetPushNotificationCountData>

data class GetPushNotificationCountData(
    var ButtonType: Int=0,
    var Position: Int=0,
    var TotalNotification: Int=0,
    var ReadNotification: Int=0,
    var UnreadNotification: Int=0

)

