package com.example.esm.notice.models

data class NoticeResponseListModel(
    val NotificationList: List<NoticeModel>,
    val NotificationTypeId: Int,
    val StudentId: Int

)
