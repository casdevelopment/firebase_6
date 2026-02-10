package com.example.esm.complaint

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
import com.example.esm.complaint.adapter.ComplaintAdapter
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.complaint.viewmodels.ComplaintViewModel
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentComplaintBinding
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ComplaintFragment : Fragment() {
    lateinit var binding: FragmentComplaintBinding
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: ComplaintViewModel by viewModel()
    var stdComplaintList: ArrayList<ComplaintModel> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_complaint,
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
        binding.showCreateComplaint.setOnClickListener {
            val action= ComplaintFragmentDirections.actionComplaintFragmentToNewComplaintFragment()
            findNavController().navigate(action)

        }
        AppConstants.USER_IDENTITY = sharedPrefsHelper.getUserId().toString()
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
            binding.complainHistoryRecyclerView.visibility= View.GONE
            binding.noItems.visibility= View.VISIBLE
            binding.showCreateComplaint.visibility = View.GONE
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
        callStdComplaintListApi(identityModel)



    }

    private fun callStdComplaintListApi(model: IdentityModel) {
        viewModel.stdComplaintList(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")

                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!= null){
                        if (apiResponse.data.isSuccessful){
                            Log.d("response","is succesful complaint fragment ")
                            val response= apiResponse.data.body()
                            if (response!= null){
                                stdComplaintList.clear()
                                response.ComplaintHistory?.let { stdComplaintList.addAll(it) }
                                setRecyclerView(stdComplaintList)

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

//    override fun onResume() {
//        setDataForApi()
//        super.onResume()
//
//    }

    private fun setRecyclerView(list: ArrayList<ComplaintModel>) {
        if (!list.isEmpty()){
            binding.complainHistoryRecyclerView.adapter = ComplaintAdapter(list, requireContext())
            binding.complainHistoryRecyclerView.visibility= View.VISIBLE
            binding.noItems.visibility = View.GONE

        }
        else{
            binding.complainHistoryRecyclerView.visibility = View.GONE
            binding.noItems.visibility = View.VISIBLE

        }

    }

}