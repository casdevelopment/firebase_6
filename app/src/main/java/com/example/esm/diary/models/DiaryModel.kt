package com.example.esm.diary.models

import com.google.gson.annotations.SerializedName

data class DiaryModel(

    val DiaryId: Int? = null,

    @SerializedName("SubjectIdFk")
    val SubjectId: Int? = null,

    val SubjectName: String? = null,
    val SubjectNameFirstLetter: String? = null,
    val CreatedBy: String? = null,

    @SerializedName("CreatedDateString")
    val CreatedDate: String? = null ,

    @SerializedName("Text")
    val DiaryNotes: String? = null,

    val UserFileName: String? = null,
    val LogoMIMEType: String? = null,
    val LogoContent: String? = null,
    val DateFromString: String? = null,

)

data class StudentDiaryRequestModel(

    val UserIdentity: String,

    val StudentId: Int,

    val DiaryId: Int
)
