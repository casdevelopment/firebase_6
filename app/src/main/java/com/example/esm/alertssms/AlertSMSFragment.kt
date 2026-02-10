package com.example.esm.alertssms

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.alertssms.adapters.AlertSmsAdapter
import com.example.esm.alertssms.models.AlertSmsModel
import com.example.esm.alertssms.viewmodels.AlertSmsViewModel
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentAlertSmsBinding
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AlertSMSFragment : Fragment() ,AlertSmsAdapter.onSmsItemClickListener{
    lateinit var binding: FragmentAlertSmsBinding

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: AlertSmsViewModel by viewModel()

    var smsHistoryList: ArrayList<AlertSmsModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_alert_sms,
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
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
//            binding.smsRecyclerView.visibility= View.GONE
//            binding.noItem.visibility= View.VISIBLE
            displayDummyData()
        } else {
            setDataForApi()
        }
    }

    private fun setDataForApi() {
        val identityModel= IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode= sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentSmsHistory(identityModel)

    }

    private fun callStudentSmsHistory(model: IdentityModel) {
        viewModel.studentSmsHistory(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!= null){
                        if (apiResponse.data.isSuccessful){
                            val response= apiResponse.data.body()
                            if (response!= null){
                                response.SMSHistory?.let{
                                    smsHistoryList.addAll(it)
                                }
                                setRecyclerView(smsHistoryList)
                            } else {
                                Toast.makeText(requireContext(), " Api response is null", Toast.LENGTH_SHORT).show()
                            }

                            Log.d("response","succesful")

                        }else{
                            Log.d("response"," not succesful")
                            Toast.makeText(requireContext(), "not successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Status.ERROR->{
                    Log.d("response","error")
                    AppUtils.stopLoader()
                }
            }


        }




    }

    private fun setRecyclerView(smsList: ArrayList<AlertSmsModel>) {
        if (!smsList.isEmpty()){
            binding.smsRecyclerView.adapter= AlertSmsAdapter(smsList,this)
            binding.smsRecyclerView.visibility= View.VISIBLE
            binding.noItem.visibility= View.GONE

        }else{
            binding.smsRecyclerView.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE

        }
    }

    override fun onItemClick(position: Int, alertSmsModel: AlertSmsModel) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.message_popup)
        val txt = dialog.findViewById<TextView>(R.id.fullMessage)
        txt.setText(alertSmsModel.Message)
        dialog.show()
        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun displayDummyData() {
        // Check if the user is logged in with a dummy number
        val smsList = ArrayList<AlertSmsModel>()
        smsList.add(
            AlertSmsModel(
                "Dear Parents, You are cordially invited to attend the upcoming Parent-Teacher Meeting at the school on Wednesday, 27th November 2024. We look forward to discussing your child’s progress and addressing any questions or concerns you may have.",
                "PTM",
                "11-11-2024",
                "Meeting"
            )
        )

        // Call setRecyclerView with the smsList
        setRecyclerView(smsList)
    }
}