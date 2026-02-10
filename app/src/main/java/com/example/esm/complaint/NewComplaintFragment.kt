package com.example.esm.complaint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.complaint.viewmodels.ComplaintViewModel
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentNewComplaintBinding
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.SharedPrefsHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewComplaintFragment : Fragment() {
     lateinit var binding: FragmentNewComplaintBinding
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: ComplaintViewModel by viewModel()
    private val activity: DashboardActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_new_complaint,
            container,
            false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitComplaint.setOnClickListener {
            if (validation()){
                val complaintModel= ComplaintModel()
                complaintModel.ComplaintId = 0
                complaintModel.ComplaintText = binding.complaintText.text.toString()
                complaintModel.ComplaintTitle = binding.titleComplaint.text.toString()
                complaintModel.Logged = 0
                complaintModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
                complaintModel.StudentId = AppConstants.STUDENT_ID.toString()
                callComplaintRegistrationApi(complaintModel)

            }

        }

    }


    private fun validation(): Boolean {
        if (binding.titleComplaint.text.toString().isEmpty()){
            binding.titleError.visibility = View.VISIBLE
            binding.titleError.setText("Please enter title for complaint")
            return false
        }
        else if (binding.complaintText.text.isEmpty()){
            binding.complaintError.visibility= View.VISIBLE
            binding.complaintError.setText("Please enter complaint details")
            return false
        }
        else {
            binding.titleError.visibility= View.GONE
            binding.complaintError.visibility= View.GONE
            return true
        }


}

    private fun callComplaintRegistrationApi(model: ComplaintModel) {
        viewModel.complaintRegistration(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")

                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!= null){
                        if (apiResponse.data.isSuccessful){
                            findNavController().popBackStack()
                           // supportFragmentManager.popBackStack("YOUR_FRAGMENT_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            // getActivity()?.finish()
                            //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                            Log.d("response"," new complaint is succesful")
//                            getFragmentManager()?.popBackStack();

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



}