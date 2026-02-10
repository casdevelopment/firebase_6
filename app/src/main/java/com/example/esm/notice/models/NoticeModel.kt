package com.example.esm.notice.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class NoticeModel(
    val NotificationText: String,
    val NotificationTitle: String,
    @SerializedName("Files")
    val NoticeFiles: ArrayList<NoticeFilesModel>

):Parcelable
