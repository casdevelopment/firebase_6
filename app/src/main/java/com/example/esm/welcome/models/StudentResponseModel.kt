package com.example.esm.welcome.models

import androidx.annotation.Keep
import com.example.esm.alertssms.models.AlertSmsModel
import com.example.esm.attendance.models.AttendanceModel
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.feecard.models.FeeCardMasterModel
import com.example.esm.hostel.models.HostelModel
import com.example.esm.paymenthistory.models.PaymentHistoryModel
import com.example.esm.results.models.ResultMasterModel
import com.example.esm.studentconsultancy.models.StudentConsultancyModel
import com.example.esm.studenttimetable.models.TimeTableModel
import com.google.gson.annotations.SerializedName
@Keep
data class StudentResponseModel(

    val EntityLogoString:String?=null,

    val StudentList: ArrayList<StudentDataModel>?= null,

    val FeeCard: List<FeeCardMasterModel>?=null,

    val FeePaymentHistory: List<PaymentHistoryModel>?=null,

    val SMSHistory: List<AlertSmsModel>?= null,

    val AttendanceHistory: ArrayList<AttendanceModel>?= null,

    val Evaluations: List<ResultMasterModel>?= null,

    val EntityLogoMIME:String?= null,

    val StudentConsultancyList: List<StudentConsultancyModel>?= null,

    val HostelInformationList: List<HostelModel>? = null,

    @SerializedName("CMS_Mobile_Complaints")
    val ComplaintHistory : List<ComplaintModel>?= null,
    val TimeTable: List<TimeTableModel>? = null
)
