package com.example.esm.welcome.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
@Keep
@Parcelize
data class StudentDataModel(
    var StudentId:Int? = null,

    var StudentName:String?= null,

    var FatherName:String?= null,

    var StudentPictureString:String?= null,

    var SchoolLogoString:String?= null,

    var MIMEType:String?= null,

    var SectionName:String?= null,

    var ClassName:String?= null,

    var SchoolName:String?=null,

    var AdmissionNumber:String?= null,

    var Facebook:String?= null,

    var Instagram:String?= null,

    var Twitter:String?= null,

    var Linkedin:String?= null,

    var Youtube:String?= null,

    var Websiteurl:String?= null

):Parcelable
