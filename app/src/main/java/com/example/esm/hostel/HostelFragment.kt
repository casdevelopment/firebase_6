package com.example.esm.hostel

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
import com.example.esm.attendance.models.AttendanceModel
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentHostelBinding
import com.example.esm.hostel.adapters.HostelAdapter
import com.example.esm.hostel.models.HostelModel
import com.example.esm.hostel.viewmodels.HostelViewModel
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

class HostelFragment : Fragment() {
    lateinit var binding: FragmentHostelBinding
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: HostelViewModel by viewModel()
    val studentHstlList: ArrayList<HostelModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_hostel,
            container,
            false
        )
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
            binding.rclHostel.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE
        } else {
            setDataForApi()
        }

        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()
    }

    private fun setDataForApi() {
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentHostelInfo(identityModel)

    }

    private fun callStudentHostelInfo(model: IdentityModel) {
        viewModel.studentHostelInfo(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")

                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!= null){
                        if (apiResponse.data.isSuccessful){
                            Log.d("response","is succesful")
                            val response= apiResponse.data.body()
                            if (response!= null){
                                response.HostelInformationList?.let { studentHstlList.addAll(it) }
                                setRecyclerView(studentHstlList)
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

    private fun setRecyclerView(list: ArrayList<HostelModel>) {
        if (!list.isEmpty()){
            binding.rclHostel.adapter = HostelAdapter(list)
            binding.hostelList.visibility= View.VISIBLE
            binding.noItem.visibility= View.GONE
        }else{
            binding.hostelList.visibility= View.GONE
            binding.noItem.visibility=View.VISIBLE
        }

    }
}