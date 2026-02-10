package com.example.esm.attendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.attendance.adapters.AttendanceAdapter
import com.example.esm.attendance.models.AttendanceModel
import com.example.esm.attendance.viewmodels.AttendanceViewModel
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentAttendanceBeconBinding
import com.example.esm.databinding.FragmentAttendanceBinding
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AttendanceFragment : Fragment() {
    lateinit var binding: FragmentAttendanceBinding
    lateinit var bindingBeacon: FragmentAttendanceBeconBinding


    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: AttendanceViewModel by viewModel()

    var attendanceHistoryList: ArrayList<AttendanceModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        binding = FragmentAttendanceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireContext().packageName.equals("com.bag.esm")) {
            binding.timeHeading.visibility= GONE

            val param = LinearLayout.LayoutParams(
                0,
                100,
                1.5f
            )
            binding.dateHeading.setLayoutParams(param)
            binding.statusHeading.setLayoutParams(param)
        }
        if (requireContext().packageName.equals("com.bass.esm")) {
            binding.timeOutHeading.visibility= VISIBLE
            binding.historyLayout.weightSum = 4f

        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                }
            })
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
//            binding.attendanceHistoryRecyclerView.visibility= View.GONE
//            binding.history.visibility = View.GONE
//            binding.noItem.visibility= View.VISIBLE
            displayDummyData()
        } else {
            setDataForApi()
        }



    }

    private fun setDataForApi() {
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentAttendanceHistory(identityModel)
    }

    private fun callStudentAttendanceHistory(model: IdentityModel) {
        viewModel.studentAttendanceHistory(model).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.isSuccessful) {
//                            Toast.makeText(requireContext(), " is successfull", Toast.LENGTH_SHORT)
//                                .show()

                            val response = apiResponse.data.body()
                            if (response != null) {
                                response.AttendanceHistory?.let { attendanceHistoryList.addAll(it) }
                                setRecyclerView(attendanceHistoryList)

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    " api response is null",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else {
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Status.ERROR -> {
                    AppUtils.stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                    Log.d("response", "error:${apiResponse.message}")

                }
            }

        }


    }

    private fun setRecyclerView(list: ArrayList<AttendanceModel>) {
        if (!list.isEmpty()) {
            binding.attendanceHistoryRecyclerView.adapter = AttendanceAdapter(list,requireContext())
            binding.history.visibility = View.VISIBLE
            binding.noItem.visibility = View.GONE

        } else {
            binding.history.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE
        }

    }

    private fun displayDummyData() {
        val attendanceList = parseJson(AppConstants.DummyJsonResponseAttendance)
        setRecyclerView(attendanceList)
    }
    private fun parseJson(json: String): ArrayList<AttendanceModel> {
        val attendanceList: ArrayList<AttendanceModel> = java.util.ArrayList()
        try {
            val jsonObject = JSONObject(json)
            val studentArray = jsonObject.getJSONArray("AttendanceHistory")
            for (i in 0 until studentArray.length()) {
                val studentObject = studentArray.getJSONObject(i)
                val attendance = AttendanceModel()

                attendance.AttendanceDateString = studentObject.getString("AttendanceDateString")

                attendance.Status = studentObject.getString("Status")
                attendance.TimeIn = studentObject.getString("TimeIn")
                attendanceList.add(attendance)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return attendanceList
    }
}