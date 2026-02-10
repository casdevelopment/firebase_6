package com.example.esm.studenttimetable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentStudentTimeTableBinding
import com.example.esm.hostel.models.HostelModel
import com.example.esm.network.Status
import com.example.esm.studenttimetable.adapters.StudentTimeTableAdapter
import com.example.esm.studenttimetable.models.TimeTableModel
import com.example.esm.studenttimetable.viewmodels.TimeTableViewModel
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentTimeTableFragment : Fragment() {

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: TimeTableViewModel by viewModel()

    lateinit var binding: FragmentStudentTimeTableBinding
    var stdTimetableList: ArrayList<TimeTableModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_student_time_table,
            container,
            false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                }
            })
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
//            binding.rclTimeTable.visibility= View.GONE
//            binding.noItem.visibility= View.VISIBLE
            displayDummyData()
        } else {
            setDataForApi()
        }
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()
    }

    private fun setDataForApi() {
        val identityModel= IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode= sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentTimeTable(identityModel)

    }

    private fun callStudentTimeTable(model: IdentityModel) {
        viewModel.stdTimeTable(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!= null){
                        if (apiResponse.data.isSuccessful){
                            Log.d("response","is succesful")
                            val response = apiResponse.data.body()
                            if (response != null){
                                response.TimeTable?.let { stdTimetableList.addAll(it) }
                                setRecyclerview(stdTimetableList)
                                Log.d("response","response of time table: $stdTimetableList")
                            }else{
                                Toast.makeText(requireContext(), " api response is null", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Log.d("response","not succesful")
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Status.ERROR->{
                    AppUtils.stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                    Log.d("response","error:${apiResponse.message}")
                }
            }
        }


    }

    private fun setRecyclerview(list: ArrayList<TimeTableModel>) {
        if (!list.isEmpty()){
            binding.rclTimeTable.adapter = StudentTimeTableAdapter(list)
            binding.hostelList.visibility = View.VISIBLE
            binding.noItem.visibility = View.GONE

        }else{
            binding.hostelList.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE
        }

    }

    private fun displayDummyData() {
        val timeTableList = parseJson(AppConstants.DummyJsonResponseTimeTable)
        setRecyclerview(timeTableList)
    }
    private fun parseJson(json: String): ArrayList<TimeTableModel> {
        val timeTableList: ArrayList<TimeTableModel> = java.util.ArrayList()
        try {
            val jsonObject = JSONObject(json)
            val studentArray = jsonObject.getJSONArray("TimeTable")
            for (i in 0 until studentArray.length()) {
                val studentObject = studentArray.getJSONObject(i)
                val timeTable = TimeTableModel()

                timeTable.SubjectName = studentObject.getString("SubjectName")
                timeTable.StartDate = studentObject.getString("StartDate")
                timeTable.EndDate = studentObject.getString("EndDate")
                timeTable.StartTime = studentObject.getString("StartTime")
                timeTable.EndTime = studentObject.getString("EndTime")

                timeTableList.add(timeTable)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return timeTableList
    }
}