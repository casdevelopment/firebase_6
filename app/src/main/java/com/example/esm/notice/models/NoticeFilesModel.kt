package com.example.esm.notice.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeFilesModel(
    val FileName: String,
    val FileURL: String

):Parcelable
