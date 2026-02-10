package com.example.esm.feecard

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
import com.example.esm.databinding.FragmentFeeCardBinding
import com.example.esm.feecard.adapters.FeeCardAdapter
import com.example.esm.feecard.models.FeeCardDetailsModel
import com.example.esm.feecard.viewmodels.FeeCardViewModel
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
import java.io.File

class FeeCardFragment : Fragment() {

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: FeeCardViewModel by viewModel()

    lateinit var binding:FragmentFeeCardBinding
    var feeCardDetailList: ArrayList<FeeCardDetailsModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fee_card,
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
//            binding.feeCardRecyclerView.visibility= View.GONE
//            binding.feeCardLayout.visibility = View.GONE
//            binding.noItem.visibility= View.VISIBLE

            binding.feeCardDuedateDate.visibility = View.GONE
            binding.tvDuedate.visibility = View.GONE
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
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentFeeCard(identityModel)

    }

    private fun callStudentFeeCard(model: IdentityModel) {
        viewModel.studentFeeCard(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!= null){
                        if (apiResponse.data.isSuccessful){
                            Log.d("response"," fee card is succesful")
                            val response= apiResponse.data.body()
                            if (response != null) {
                                binding.feeCardDuedateDate.text= response.StringDueDate
                                response.feeCardDetail?.let { feeCardDetailList.addAll(it) }
                                setRecyclerView(feeCardDetailList)
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

    private fun setRecyclerView(list: ArrayList<FeeCardDetailsModel>) {
        if (!list.isEmpty()){
            var total:Long = 0
            binding.feeCardRecyclerView.adapter = FeeCardAdapter(list)
            binding.feeCardLayout.visibility = View.VISIBLE
            binding.noItem.visibility = View.GONE
            for (item in list){
                total= item.TRemaining?.let { total.plus(it) }!!
            }
            binding.total.text= total.toString()

        }
        else{
            binding.feeCardLayout.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE
        }

    }

    private fun displayDummyData() {
        val feeList = parseJson(AppConstants.DummyJsonResponseFee)
        Log.v("fee",feeList.toString())

        setRecyclerView(feeList)
    }

    private fun parseJson(json: String): ArrayList<FeeCardDetailsModel> {
        val feetList: ArrayList<FeeCardDetailsModel> = java.util.ArrayList()
        try {
            val jsonObject = JSONObject(json)
            val studentArray = jsonObject.getJSONArray("cMS_Mobile_FeeCardDetail")
            for (i in 0 until studentArray.length()) {
                val studentObject = studentArray.getJSONObject(i)
                val fee = FeeCardDetailsModel()
                fee.TFeeHeadName = studentObject.getString("TFeeHeadName")
                Log.v("fee",fee.TFeeHeadName.toString())
                fee.TPrevBalance = studentObject.getInt("TPrevBalance").toLong()
                fee.TFeeHeadActualAmount = studentObject.getInt("TFeeHeadActualAmount").toLong()
                fee.TRemaining = studentObject.getInt("TRemaining").toLong()
                //     binding.feeCardDuedateDate.text = studentObject.getString("StringDueDate")

                feetList.add(fee)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return feetList
    }
}