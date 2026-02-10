package com.example.esm.network.koin_module

import android.util.Log
import androidx.annotation.Keep
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.complaint.models.ComplaintResponseList
import com.example.esm.complaint.models.ComplaintResponseModel
import com.example.esm.dashboardfragment.models.DashboardFragmentModel
import com.example.esm.diary.models.DiaryResponseModel
import com.example.esm.eventcalendar.models.EventsResponseModel
import com.example.esm.feecard.models.FeeCardMasterModel
import com.example.esm.login.models.LoginRequestResponseModel
import com.example.esm.network.ApiInterface
import com.example.esm.notice.models.NoticeModel
import com.example.esm.notice.models.NoticeResponseListModel
import com.example.esm.policy.PolicyData
import com.example.esm.report.ReportDataModel
import com.example.esm.signup.model.SignUpRequestResponseModel
import com.example.esm.studenttimetable.models.TimeTableModel
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import org.json.JSONObject
import retrofit2.Response
@Keep
class Repository(private val apiInterface: ApiInterface) {
    suspend fun loginApi(model: LoginRequestResponseModel): Response<String> {
        return apiInterface.loginApi(model)

    }

    suspend fun studentListApi(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.studentListApi(model)

    }

    suspend fun stdFeePaymentHistory(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.stdFeePaymentHistory(model)

    }

    suspend fun studentAttendanceHistory(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.studentAttendanceHistory(model)

    }

    suspend fun stdMonthlyExamHistory(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.stdMonthlyExamHistory(model)

    }
    suspend fun downloadResultApi(model: IdentityModel): Response<String> {
        return apiInterface.downloadResultApi(model)

    }
    suspend fun downloadWeeklyResultApi(model: IdentityModel): Response<String> {
        return apiInterface.downloadWeeklyResultApi(model)

    }

    suspend fun studentSmsHistory(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.studentSmsHistory(model)

    }

    suspend fun studentFeeCard(model: IdentityModel): Response<FeeCardMasterModel> {
        return apiInterface.studentFeeCard(model)

    }

    suspend fun stdTimeTable(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.stdTimeTable(model)

    }

    suspend fun studentConsultancy(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.studentConsultancy(model)

    }

    suspend fun studentHostelInfo(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.studentHostelInfo(model)

    }

    suspend fun stdComplaintList(model: IdentityModel): Response<StudentResponseModel> {
        return apiInterface.stdComplaintList(model)

    }

    suspend fun complaintRegistration(model: ComplaintModel): Response<ComplaintModel> {
        return apiInterface.complaintRegistration(model)

    }

    suspend fun complaintResponseList(model: ComplaintResponseList): Response<ComplaintResponseList> {
        return apiInterface.complaintChatResponseList(model)
    }

//    suspend fun submitComplaintMessage(model: ComplaintResponseModel): Response<ComplaintModel> {
//        return apiInterface.submitCompliantResponse(model)
//    }
    suspend fun submitComplaintMessage(fields: HashMap<String, Any>): Response<ComplaintResponseModel> {
        return apiInterface.submitCompliantResponse(fields)
    }

    suspend fun downloadVoucherApi(model: DashboardFragmentModel): Response<String> {
        return apiInterface.downloadVoucherApi(model)
    }

    suspend fun downloadVoucherApiAlRazi(model: DashboardFragmentModel): Response<String> {
        return apiInterface.downloadVoucherApiAlRazi(model)
    }

     suspend fun getStudentDiary(model: IdentityModel): Response<DiaryResponseModel> {
         return apiInterface.getStudentDiary(model)

    }

     suspend fun signUpApi(model: SignUpRequestResponseModel): Response<SignUpRequestResponseModel> {
         return apiInterface.signUpApi(model)

    }

     suspend fun studentNoticeApi(model: IdentityModel): Response<List<NoticeModel>> {
        return apiInterface.studentNoticeApi(model)

    }

   suspend fun calendarList(model: IdentityModel): Response<EventsResponseModel> {
        return apiInterface.calendarList(model)

    }

    suspend fun updatePassword(fields: HashMap<String, String>): Response<String> {
        Log.v("showRunning", "updatePassword apiInterface  " )
        return apiInterface.updatePassword(fields)
    }
    suspend fun saveNotificationApi(fields: HashMap<String, String>): Response<String> {
        Log.v("showRunning", "updatePassword apiInterface  " )
        return apiInterface.saveNotificationApi(fields)
    }
    suspend fun applyLeave(fields: HashMap<String, String>): Response<JSONObject> {
        Log.v("showRunning", "applyLeave apiInterface  " )
        return apiInterface.applyLeave(fields)
    }

    suspend fun getPolicyListBeacon(fields: HashMap<String, String>): Response<PolicyData> {
        return apiInterface.getPolicyListBeacon(fields)
    }

    suspend fun getEvaluationType(fields: HashMap<String, Any>): Response<ReportDataModel> {
        return apiInterface.getEvaluationType(fields)
    }

    suspend fun getMiddleEvaluationType(fields: HashMap<String, Any>): Response<ReportDataModel> {
        return apiInterface.getMiddleEvaluationType(fields)
    }

    suspend fun getOALevelEvaluationType(fields: HashMap<String, Any>): Response<ReportDataModel> {
        return apiInterface.getOALevelEvaluationType(fields)
    }

    suspend fun getEvaluation(fields: HashMap<String, Any>): Response<ReportDataModel> {
        return apiInterface.getEvaluation(fields)
    }

    suspend fun getMiddleEvaluation(fields: HashMap<String, Any>): Response<ReportDataModel> {
        return apiInterface.getMiddleEvaluation(fields)
    }

    suspend fun getOALevelEvaluation(fields: HashMap<String, Any>): Response<ReportDataModel> {
        return apiInterface.getOALevelEvaluation(fields)
    }
    suspend fun getMYEReport(fields: HashMap<String, Any>): Response<String> {
        return apiInterface.getMYEReport(fields)
    }
    suspend fun getEOYReport(fields: HashMap<String, Any>): Response<String> {
        return apiInterface.getEOYReport(fields)
    }
    suspend fun getMiddleReport(fields: HashMap<String, Any>): Response<String> {
        return apiInterface.getMiddleReport(fields)
    }
    suspend fun getOALevelReport(fields: HashMap<String, Any>): Response<String> {
        return apiInterface.getOALevelReport(fields)
    }


}