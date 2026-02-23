package com.example.esm.network

import androidx.annotation.Keep
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.complaint.models.ComplaintResponseList
import com.example.esm.complaint.models.ComplaintResponseModel
import com.example.esm.dashboardfragment.models.DashboardFragmentModel
import com.example.esm.diary.models.DiaryResponseModel
import com.example.esm.eventcalendar.models.EventsResponseModel
import com.example.esm.feecard.models.FeeCardMasterModel
import com.example.esm.login.models.LoginRequestResponseModel
import com.example.esm.notice.models.NoticeModel
import com.example.esm.paymenthistory.models.PaymentHistoryPostModel
import com.example.esm.policy.PolicyData
import com.example.esm.report.ReportDataModel
import com.example.esm.signup.model.SignUpRequestResponseModel
import com.example.esm.utils.AppConstants
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Keep
interface ApiInterface {
    @POST(AppConstants.LOGIN)
    suspend fun loginApi(@Body model: LoginRequestResponseModel): Response<String>

    @POST(AppConstants.SIGNUP)
    suspend fun signUpApi(@Body model: SignUpRequestResponseModel): Response<SignUpRequestResponseModel>

    @POST(AppConstants.STUDENT_LIST)
    suspend fun studentListApi(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_FEEPAYMENT_HISTORY)
    suspend fun stdFeePaymentHistory(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_ATTENDANCE_HISTORY)
    suspend fun studentAttendanceHistory(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_EXAM_HISTORY)
    suspend fun stdMonthlyExamHistory(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.DownloadResultOnMobileApp)
    suspend fun downloadResultApi(@Body model: IdentityModel): Response<String>

    @POST(AppConstants.DownloadWeeklyResultOnMobileApp)
    suspend fun downloadWeeklyResultApi(@Body model: IdentityModel): Response<String>

    @POST(AppConstants.STD_SMS_HISTORY)
    suspend fun studentSmsHistory(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_FEE_CARD)
    suspend fun studentFeeCard(@Body model: IdentityModel): Response<FeeCardMasterModel>

    @POST(AppConstants.STD_TIME_TABLE)
    suspend fun stdTimeTable(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_CONSULTANCY)
    suspend fun studentConsultancy(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_HOSTEL_INFO)
    suspend fun studentHostelInfo(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_COMPLAINT_LIST)
    suspend fun stdComplaintList(@Body model: IdentityModel): Response<StudentResponseModel>

    @POST(AppConstants.STD_COMPLAINT_REGISTRATION)
    suspend fun complaintRegistration(@Body model: ComplaintModel): Response<ComplaintModel>

    @POST(AppConstants.STD_COMPLAINT_Response_List)
    suspend fun complaintChatResponseList(@Body model: ComplaintResponseList): Response<ComplaintResponseList>

    //    @POST("Mobile/AddComplaintResponse")
//    suspend fun submitCompliantResponse(@Body complaint: ComplaintResponseModel): Response<ComplaintModel>
    @POST("AddComplaintResponse")
    suspend fun submitCompliantResponse(@Body fields: HashMap<String, Any>): Response<ComplaintResponseModel>

    @POST(AppConstants.DOWNLOAD_VOUCHER)
    suspend fun downloadVoucherApi(@Body model: DashboardFragmentModel): Response<String>

    @POST(AppConstants.STUDENT_DIARY)
    suspend fun getStudentDiary(@Body model: IdentityModel): Response<DiaryResponseModel>

    @POST(AppConstants.STUDENT_NOTIFICATION)
    suspend fun studentNoticeApi(@Body model: IdentityModel): Response<List<NoticeModel>>

    @POST(AppConstants.STUDENT_CALENDAR)
    suspend fun calendarList(@Body model: IdentityModel): Response<EventsResponseModel>

    // for Al_razi
    @POST("GetVocuher")
    suspend fun downloadVoucherApiAlRazi(@Body model: DashboardFragmentModel): Response<String>

    // for rubrics
    @POST("ChangePassword")
    suspend fun updatePassword(@Body fields: HashMap<String, String>): Response<String>

    // for Beacon
    @POST("ChangePassword")
    suspend fun saveNotificationApi(@Body fields: HashMap<String, String>): Response<String>

    @POST("MobileStudentAppliedLeave")
    suspend fun applyLeave(@Body fields: HashMap<String, String>): Response<JSONObject>

    // for Beacon
    @POST("GetPolicyList_Beacon")
    suspend fun getPolicyListBeacon(@Body fields: HashMap<String, String>): Response<PolicyData>


    @POST("GetEvaluationTypeForExamResult_Mobile")
    suspend fun getEvaluationType(@Body fields: HashMap<String, Any>): Response<ReportDataModel>

    @POST("GetEvaluationTypeTest_MiddleSchool_Mobile")
    suspend fun getMiddleEvaluationType(@Body fields: HashMap<String, Any>): Response<ReportDataModel>

    @POST("GetEvaluationTypeTest_OALevel_Mobile")
    suspend fun getOALevelEvaluationType(@Body fields: HashMap<String, Any>): Response<ReportDataModel>

    @POST("GetEvaluationForExamResult_Mobile")
    suspend fun getEvaluation(@Body fields: HashMap<String, Any>): Response<ReportDataModel>

    @POST("GetEvaluationTest_MiddleSchool_Mobile")
    suspend fun getMiddleEvaluation(@Body fields: HashMap<String, Any>): Response<ReportDataModel>

    @POST("GetEvaluationTest_OALevel_Mobile")
    suspend fun getOALevelEvaluation(@Body fields: HashMap<String, Any>): Response<ReportDataModel>

    @POST("DownloadMYEReport_Mobile")
    suspend fun getMYEReport(@Body fields: HashMap<String, Any>): Response<String>

    @POST("DownloadEOYReport_Mobile")
    suspend fun getEOYReport(@Body fields: HashMap<String, Any>): Response<String>

    @POST("DownloadMiddleSchoolTestReport_Mobile")
    suspend fun getMiddleReport(@Body fields: HashMap<String, Any>): Response<String>

    @POST("DownloadOALevelTestReport_Mobile")
    suspend fun getOALevelReport(@Body fields: HashMap<String, Any>): Response<String>

    @POST("DownloadFeeReceipt")
    suspend fun downloadFeeReceipt(@Body model: PaymentHistoryPostModel): Response<String>

}