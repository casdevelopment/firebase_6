package com.example.esm.paymenthistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentPaymentHistoryBinding
import com.example.esm.network.Status
import com.example.esm.paymenthistory.adapters.PaymentHistoryAdapter
import com.example.esm.paymenthistory.adapters.PaymentHistoryAdapter.OnPaymentClickListener
import com.example.esm.paymenthistory.models.PaymentHistoryModel
import com.example.esm.paymenthistory.models.PaymentHistoryPostModel
import com.example.esm.paymenthistory.viewmodels.PaymentViewModel
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentHistoryFragment : Fragment() , OnPaymentClickListener{
    lateinit var binding: FragmentPaymentHistoryBinding

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: PaymentViewModel by viewModel()
    private val countDecoration = 0


    var stdFeePaymentHistoryList: ArrayList<PaymentHistoryModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_payment_history,
            container,
            false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()

        if (!requireContext().packageName.equals("com.fssa.esm")) {
            binding.headerTvLayout.weightSum = 2.0f
            // 2. Hide the Fee Receipt column
            binding.feeReceiptTv.visibility = View.GONE
            // Optional: Ensure the layout refreshes
            binding.headerTvLayout.requestLayout()

        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                }
            })
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
//            binding.paymentHistoryRecyclerView.visibility= View.GONE
//            binding.history.visibility= View.GONE
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
        callStudentFeePaymentHistoryApi(identityModel)
    }

    private fun callStudentFeePaymentHistoryApi(model: IdentityModel) {
        viewModel.stdFeePaymentHistory(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!=null){
                        if (apiResponse.data.isSuccessful){
                            Log.d("response","is succesful")
                            val response= apiResponse.data.body()
                            if (response != null) {
                                response.FeePaymentHistory?.let { stdFeePaymentHistoryList.addAll(it) }
                                setRecyclerView(stdFeePaymentHistoryList)
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



    private fun setRecyclerView(list: ArrayList<PaymentHistoryModel>) {
        if (!list.isEmpty()){
            binding.paymentHistoryRecyclerView.adapter= PaymentHistoryAdapter(list,this,requireContext())
            binding.history.visibility= View.VISIBLE
            binding.noItem.visibility= View.GONE


        }
        else{
            binding.history.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE

        }

    }

    private fun displayDummyData() {
        val paymentList = parseJson(AppConstants.DummyJsonResponsePaymentHistory)
        setRecyclerView(paymentList)
    }
    private fun parseJson(json: String): ArrayList<PaymentHistoryModel> {
        val paymentList: ArrayList<PaymentHistoryModel> = java.util.ArrayList()
        try {
            val jsonObject = JSONObject(json)
            val studentArray = jsonObject.getJSONArray("FeePaymentHistory")
            for (i in 0 until studentArray.length()) {
                val studentObject = studentArray.getJSONObject(i)
                val payment = PaymentHistoryModel()

                payment.Date = studentObject.getString("Date")
                payment.Amount = studentObject.getString("Amount")

                paymentList.add(payment)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return paymentList
    }

    override fun onDownloadReceiptClick(feeDepositId: String, ucSchoolid: String?) {

        if(feeDepositId.isEmpty()){
            Toast.makeText(requireContext(), "No receipt available", Toast.LENGTH_SHORT).show()
        }else{
            Log.v("onDownloadReceiptClick","FeeDepositId "+feeDepositId)
            val paymentHistoryPostModel= PaymentHistoryPostModel()
            paymentHistoryPostModel.UserIdentity = sharedPrefsHelper.getUserId()?.toString()
            paymentHistoryPostModel.StudentId = AppConstants.STUDENT_ID.toString()
            paymentHistoryPostModel.SchoolId = ucSchoolid
            paymentHistoryPostModel.FeeDepositeId = feeDepositId
            callDownloadReceiptApi(paymentHistoryPostModel)

        }

    }

    private fun callDownloadReceiptApi(model: PaymentHistoryPostModel) {
        viewModel.downloadFeeReceipt(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!=null){
                        if (apiResponse.data.isSuccessful){
                            Log.d("response","is succesful")
                            val response= apiResponse.data.body()
                            if (response != null) {
                                Log.v("onDownloadReceiptClick", "voucher url $response")
                                AppUtils.goToLink(response,requireContext())
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
}