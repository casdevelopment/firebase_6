package com.example.esm.studentconsultancy

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
import com.example.esm.databinding.FragmentStudentConsultancyBinding
import com.example.esm.network.Status
import com.example.esm.studentconsultancy.adapters.StudentConsultancyAdapter
import com.example.esm.studentconsultancy.models.StudentConsultancyModel
import com.example.esm.studentconsultancy.viewmodels.StudentConsultancyViewModel
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentConsultancyFragment : Fragment() {
    lateinit var binding: FragmentStudentConsultancyBinding
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: StudentConsultancyViewModel by viewModel()
    val stdConsultancyList: ArrayList<StudentConsultancyModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_student_consultancy,
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
            binding.rclStudentConsultancy.visibility= View.GONE
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
        identityModel.MobileCode= sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentConsultancyApi(identityModel)

    }

    private fun callStudentConsultancyApi(model: IdentityModel) {
        viewModel.studentConsultancy(model).observe(viewLifecycleOwner){ apiResponse->
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
                                response.StudentConsultancyList?.let { stdConsultancyList.addAll(it) }
                                setRecyclerView(stdConsultancyList)
                            }else{
                                Toast.makeText(requireContext(), " api response is null", Toast.LENGTH_SHORT).show()
                            }

                        }else{
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

    private fun setRecyclerView(list: ArrayList<StudentConsultancyModel>) {
        if (!list.isEmpty()){
            binding.rclStudentConsultancy.adapter = StudentConsultancyAdapter(list)
            binding.studentConsultancyList.visibility= View.VISIBLE
            binding.noItem.visibility= View.GONE
        }
        else{
            binding.studentConsultancyList.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE
        }

    }
}