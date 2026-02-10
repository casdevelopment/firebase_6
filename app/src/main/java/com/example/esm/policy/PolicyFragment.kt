package com.example.esm.policy

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
import com.example.esm.databinding.FragmentPolicyBinding
import com.example.esm.network.Status
import com.example.esm.paymenthistory.adapters.PaymentHistoryAdapter
import com.example.esm.paymenthistory.models.PaymentHistoryModel
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

class PolicyFragment : Fragment(), PolicyAdapter.onItemClickListener {
    lateinit var binding: FragmentPolicyBinding

    val policyList = arrayListOf(
        PolicyListData("1", "General Policy"),
        PolicyListData("2", "Discipline  Policy"),
        PolicyListData("3", "Fee  Policy"),
        PolicyListData("4", "Admission  Policy"),
        PolicyListData("5", "Other")

    )

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: PolicyViewModel by viewModel()
    private val countDecoration = 0


   // var policyList: ArrayList<PolicyListData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_policy,
            container,
            false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                        findNavController().popBackStack(R.id.dashboardFragment, false)


                }
            })
          //  callPolicyListApi()
        setRecyclerView(policyList)

    }



    private fun callPolicyListApi(policyCategoryId:String) {
        val fields: HashMap<String, String> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["PolicyCategoryId"] = policyCategoryId
        viewModel.getPolicyListBeacon(fields).observe(viewLifecycleOwner){ apiResponse->
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
                                Log.d("response","is succesful")

                                //policyList1.clear()
                                policyList.clear()
                                response.let { policyList.addAll(it.Data?.PolicyList!!) }
                                setRecyclerView(policyList)
                            }else{
                                Toast.makeText(requireContext(), "  No data found", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Log.d("response","not succesful")
                            Toast.makeText(requireContext(), " No data found", Toast.LENGTH_SHORT).show()
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



    private fun setRecyclerView(list: ArrayList<PolicyListData>) {
        if (list.isNotEmpty()){
            binding.policyRecyclerView.adapter= PolicyAdapter(list,this)
            binding.history.visibility= View.VISIBLE
            binding.noItem.visibility= View.GONE


        }
        else{
            binding.history.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE

        }

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

    override fun onItemClick(position: Int) {
        callPolicyListApi(policyList[position].PolicyId.toString())
    }
}