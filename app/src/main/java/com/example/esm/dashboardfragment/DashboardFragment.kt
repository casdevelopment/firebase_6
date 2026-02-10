package com.example.esm.dashboardfragment

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.dashboardfragment.models.DashboardFragmentModel
import com.example.esm.dashboardfragment.models.DashboardItemModel
import com.example.esm.dashboardfragment.viewmodels.DashboardFragmentViewModel
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppConstants.STUDENT_ID
import com.example.esm.utils.AppUtils
import com.example.esm.utils.AppUtils.startLoader
import com.example.esm.utils.AppUtils.stopLoader
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


open class DashboardFragment : Fragment() {

   // lateinit var binding: FragmentDashboardBinding
    private var activity: DashboardActivity? = null

    var url: URL? = null
    var filepath = ""
    var filename = ""
    var mProgressDialog: ProgressDialog? = null

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: DashboardFragmentViewModel by viewModel()

    var permissionCode = 101
    var arrayList:ArrayList<DashboardItemModel> = ArrayList()

    private lateinit var paymentHistory : RelativeLayout
    private lateinit var attendance : RelativeLayout
    private lateinit var results : RelativeLayout
    private lateinit var downLoadVoucher : RelativeLayout
    private lateinit var notice : RelativeLayout
    private lateinit var feeCard : RelativeLayout
    private lateinit var eventsCalendar : RelativeLayout
    private lateinit var complaint : RelativeLayout
    private lateinit var diary : RelativeLayout
    private lateinit var homeWork : RelativeLayout
    private lateinit var timeTable : RelativeLayout
    private lateinit var alertSms : RelativeLayout
    private lateinit var hostel : RelativeLayout
    private lateinit var studentConsultancy : RelativeLayout

    private lateinit var llDiary : LinearLayout
    private lateinit var llHostel : LinearLayout
    private lateinit var llStudentConsultancy : LinearLayout
    private lateinit var llTimeTable : LinearLayout
    private lateinit var complainLayout : LinearLayout
    private lateinit var llHomeWork : LinearLayout
    private lateinit var llPolicy : LinearLayout
    private lateinit var llLeave : LinearLayout
    private lateinit var tvDiaryText : TextView
    private lateinit var complainText : TextView
    private lateinit var complainIcon : ImageView
    private lateinit var row4 : View
    private lateinit var row5 : View
    private lateinit var row2 : View
    private lateinit var row3 : View
    private lateinit var row1 : View
    private lateinit var llReportLayout : LinearLayout
    private lateinit var downLoadVoucherLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (requireContext().packageName.equals("com.fssa.esm")){
            inflater.inflate(R.layout.fragment_dashboard_fssa, container, false)
        } else {
            inflater.inflate(R.layout.fragment_dashboard, container, false)
        }

//            binding = FragmentDashboardBinding.inflate(inflater)
//            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as DashboardActivity?

        paymentHistory = view.findViewById(R.id.payment_history)
        attendance = view.findViewById(R.id.attendance)
        results = view.findViewById(R.id.results)
        downLoadVoucher = view.findViewById(R.id.downLoadVoucher)
        notice = view.findViewById(R.id.notice)
        feeCard = view.findViewById(R.id.fee_card)
        eventsCalendar = view.findViewById(R.id.events_calendar)
        complaint = view.findViewById(R.id.complaint)
        diary = view.findViewById(R.id.diary)
        timeTable = view.findViewById(R.id.timeTable)
        alertSms = view.findViewById(R.id.alert_sms)
        hostel = view.findViewById(R.id.hostel)
        studentConsultancy = view.findViewById(R.id.student_consultancy)
        homeWork = view.findViewById(R.id.homeWork)

        llDiary = view.findViewById(R.id.ll_diary)
        llHostel = view.findViewById(R.id.ll_hostel)
        llStudentConsultancy = view.findViewById(R.id.ll_studentConsultancy)
        llTimeTable = view.findViewById(R.id.ll_timeTable)
        complainLayout = view.findViewById(R.id.complainLayout)
        llHomeWork = view.findViewById(R.id.ll_homeWork)
        tvDiaryText = view.findViewById(R.id.tv_diaryText)
        row4 = view.findViewById(R.id.row4)
        row5 = view.findViewById(R.id.row5)
        row2 = view.findViewById(R.id.row2)
        row3 = view.findViewById(R.id.row3)
        row1 = view.findViewById(R.id.row1)

         if (requireContext().packageName.equals("com.fssa.esm")) {
             downLoadVoucherLayout = view.findViewById(R.id.downLoadVoucherLayout)
             downLoadVoucherLayout.visibility = View.GONE
             llHostel.visibility = View.GONE
             row5.visibility = View.GONE

        }

            hideLayout()


        initView()

    }
    fun hideLayout(){
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
            row2.visibility = View.GONE
            row3.visibility = View.GONE
            row4.visibility = View.GONE
            row5.visibility = View.GONE
        }
    }

    private fun initView() {
        setUpNavigation()
    }



    private fun setUpNavigation() {

        paymentHistory.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToPaymentHistoryFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }


        }
        attendance.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToAttendanceFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        results.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToResultsFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        alertSms.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToAlertSMSFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        notice.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToNoticeFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        feeCard.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToFeeCardFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        eventsCalendar.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToEventsCalendarFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        complaint.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToComplaintFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        diary.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToDiaryFragment(1)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        homeWork.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToDiaryFragment(2)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        hostel.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action = DashboardFragmentDirections.actionDashboardFragmentToHostelFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        studentConsultancy.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToStudentConsultancyFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        downLoadVoucher.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                    Toast.makeText(requireContext(), "No Voucher Available", Toast.LENGTH_SHORT).show()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        callDownloadFeeVoucherApi()
                    } else {
                        checkPermission()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        timeTable.setOnClickListener {
            if(AppUtils.checkConnectivity(requireActivity())){
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToStudentTimeTableFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        if (requireContext().packageName.equals("com.bass.esm")) {
            llPolicy.setOnClickListener {
                if(AppUtils.checkConnectivity(requireActivity())){
                    val action =
                        DashboardFragmentDirections.actionDashboardFragmentToPolicyFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        if (requireContext().packageName.equals("com.tag.esm")) {
            llReportLayout.setOnClickListener {
                if (AppUtils.checkConnectivity(requireActivity())) {
                    showReportDialog()
                } else {
                    Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUpNavigation()
        activity?.drawIconVisible()
        activity?.backArrowInvisible()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ), permissionCode
            )

        } else {
            callDownloadFeeVoucherApi()

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callDownloadFeeVoucherApi()


            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), permissionCode
                )

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun callDownloadFeeVoucherApi() {
        val dashboardFragmentModel = DashboardFragmentModel()
        dashboardFragmentModel.StudentId = AppConstants.STUDENT_ID
        dashboardFragmentModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        dashboardFragmentModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        if (requireContext().packageName.equals("com.ari.esm")){
            callDownloadFeeVoucherApiAlRazi(dashboardFragmentModel)
        } else {
            callDownloadFeeVoucherApi(dashboardFragmentModel)
        }
    }

    private fun callDownloadFeeVoucherApi(model: DashboardFragmentModel) {
        viewModel.downloadVoucherApi(model).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    startLoader(requireActivity())
                    Log.d("voucher", "loading")
                }

                Status.SUCCESS -> {
                    stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.isSuccessful) {
                            Log.d("voucher", " vocuher is succesful")
                            filepath = apiResponse.data.body().toString()
                            Log.d("voucher", "$filepath")
                            try {
                                url = URL(filepath)
                            } catch (e: MalformedURLException) {
                                e.printStackTrace()
                            }
                            filename = "ESM_" + System.currentTimeMillis() + ".pdf"
                            if (filepath != "" || filepath.isNotEmpty()) {
                                startLoader(requireActivity())
                                AppUtils.downloadPdfFile(url.toString(),filename,requireContext())

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No File Found",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No File Found",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }

                Status.ERROR -> {
                    stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //for Al_razi download voucher
    private fun callDownloadFeeVoucherApiAlRazi(model: DashboardFragmentModel) {
        viewModel.downloadVoucherApiAlRazi(model).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    startLoader(requireActivity())
                    Log.d("voucher", "loading")
                }

                Status.SUCCESS -> {
                    stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.isSuccessful) {
                            Log.d("voucher", " vocuher is succesful")
                            filepath = apiResponse.data.body().toString()
                            Log.d("voucher", "$filepath")
                            try {
                                url = URL(filepath)
                            } catch (e: MalformedURLException) {
                                e.printStackTrace()
                            }
                            filename = "ESM_" + System.currentTimeMillis() + ".pdf"
                            if (filepath != "" || filepath.isNotEmpty()) {
                                startLoader(requireActivity())
                                AppUtils.downloadPdfFile(url.toString(),filename,requireContext())

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No File Found",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }
                }
                Status.ERROR -> {
                    stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showLeaveApplicationDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.leave_dialog)
        dialog.setCancelable(true)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)

        // Make dialog background transparent
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)



         val fromDateCalendar: Calendar = Calendar.getInstance()
         val toDateCalendar: Calendar = Calendar.getInstance()

        // Get references to dialog views
        val etReason = dialog.findViewById<EditText>(R.id.etReason)
        val  etFromDate = dialog.findViewById<EditText>(R.id.etFromDate)
        val  etToDate = dialog.findViewById<EditText>(R.id.etToDate)
        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        // Set up date pickers
        setupDatePicker(etFromDate, fromDateCalendar)
        setupDatePicker(etToDate, toDateCalendar)

        // Set current date as default
        updateDateLabel(etFromDate, fromDateCalendar)
        updateDateLabel(etToDate, toDateCalendar)

        // Submit button click
        btnSubmit.setOnClickListener {
            val reason = etReason.text.toString().trim()
            val fromDate = etFromDate.text.toString().trim()
            val toDate = etToDate.text.toString().trim()



            if (reason.isEmpty()) {
                etReason.error = "Please enter reason for leave"
                return@setOnClickListener
            }

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate that to date is after from date
            if (fromDateCalendar.after(toDateCalendar)) {
                Toast.makeText(requireContext(), "To date must be after from date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Here you would typically process the leave application
            /*val message = "Leave application submitted:\n" +
                    "Reason: $reason\n" +
                    "From: $fromDate\n" +
                    "To: $toDate"*/


            val fields = HashMap<String, String>()
            fields["MobileCode"] = AppConstants.MOBILE_CODE.toString()
            fields["reason"] = reason
            fields["StudentId"] = STUDENT_ID.toString()
            fields["FromDate"] =  fromDate
            fields["ToDate"] =  toDate
            fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()


            applyLeaveApi(fields)

            // Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        // Cancel button click
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupDatePicker(editText: EditText, calendar: Calendar) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(editText, calendar)
        }

        editText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateLabel(editText: EditText, calendar: Calendar) {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        editText.setText(sdf.format(calendar.time))
    }

    private fun applyLeaveApi(fields: HashMap<String, String>) {
        viewModel.applyLeave(fields).observe(viewLifecycleOwner) {apiResponse->
            when (apiResponse.status) {
                Status.LOADING -> {
                    startLoader(requireActivity())
                    Log.d("applyLeave", "loading")
                }

                Status.SUCCESS -> {
                    stopLoader()
                    if (apiResponse.data != null) {
                        Log.d("applyLeave", " vocuher is succesful")
                        Toast.makeText(requireContext(), "Submitted", Toast.LENGTH_LONG).show()

                    }
                }
                Status.ERROR -> {
                    stopLoader()
                    Log.d("applyLeave", " ERROR")
                }
            }
        }
    }



    fun showReportDialog() {
        val options = arrayOf("MYE Report", "EOY Report","Middle Class Test Report","OALevel Test Report")

        AlertDialog.Builder(requireContext())
            .setTitle("Select Report")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val action =
                            DashboardFragmentDirections.actionDashboardFragmentToReportFragment(true,false,false,false)
                        findNavController().navigate(action)
                    }
                    1 -> {
                        val action =
                            DashboardFragmentDirections.actionDashboardFragmentToReportFragment(false,true,false,false)
                        findNavController().navigate(action)
                    }
                    2 -> {
                        val action =
                            DashboardFragmentDirections.actionDashboardFragmentToReportFragment(false,false,false,true)
                        findNavController().navigate(action)
                    }3 -> {
                    val action =
                        DashboardFragmentDirections.actionDashboardFragmentToReportFragment(false,false,true,false)
                    findNavController().navigate(action)
                }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}