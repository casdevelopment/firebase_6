package com.example.esm.notice.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.esm.R
import com.example.esm.databinding.FragmentCommonApiBinding
import com.example.esm.databinding.FragmentNoticeViewBinding
import com.example.esm.network.Status
import com.example.esm.notice.adapter.RecyclerViewAdapter
import com.example.esm.notice.adapter.RecyclerViewFileAdapter
import com.example.esm.notice.models.NoticeFilesModel
import com.example.esm.notice.models.NoticeModel
import com.example.esm.notice.viewModel.CommonApiViewModel
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommonApiFragment : Fragment() ,RecyclerViewAdapter.onNoticeClickListener, RecyclerViewFileAdapter.onItemClickListener {
    lateinit var binding: FragmentCommonApiBinding
    private val args:CommonApiFragmentArgs by navArgs()
    val sharedPrefsHelper: SharedPrefsHelper by inject()

    private val viewModel: CommonApiViewModel by viewModel()

    var studentNoticeList: ArrayList<NoticeModel> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_common_api,
            container,
            false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.noticeFragment, false)
                }
            })
        val id = args.notificationId
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
            binding.recyclerView.visibility= View.GONE
            binding.noItems.visibility= View.VISIBLE
        } else {
            setDataForApi()
        }
    //  Toast.makeText(requireContext(), "id is: $id", Toast.LENGTH_SHORT).show()
    }

    private fun setDataForApi() {
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = args.notificationId
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        callStudentNoticeApi(identityModel)

    }

    private fun callStudentNoticeApi(model: IdentityModel) {
        viewModel.studentNoticeApi(model).observe(viewLifecycleOwner){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(requireActivity())
                    Log.d("response","loading")
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data != null){
                        if (apiResponse.data.isSuccessful){
                            Log.e("response","is succesful")
                           // Toast.makeText(requireContext(), "is successful", Toast.LENGTH_SHORT).show()
                            var list : ArrayList<NoticeModel> = ArrayList()
                              apiResponse.data.body()?.let { list.addAll(it) }
                              setRecyclerView(list)

                        }
                    }
                }
                Status.ERROR->{
                    AppUtils.stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                    Log.v("response","error:${apiResponse.message}")

                }
            }

        }
    }

    private fun setRecyclerView(list: ArrayList<NoticeModel>) {
        if (list != null && list.isNotEmpty()){
            if (binding != null && binding.recyclerView != null) {
                binding.recyclerView.adapter = RecyclerViewAdapter(list, this)
                binding.noItems.visibility = View.GONE
            }else {
                Log.e("RecyclerView", "Binding or RecyclerView is null")
            }
        }
        else{
            binding.noItems.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(model: NoticeModel, position: Int) {
//        val action = CommonApiFragmentDirections.actionCommonApiFragmentToNoticeViewFragment(model)
//        findNavController().navigate(action)
        val dialogBinding = FragmentNoticeViewBinding.inflate(LayoutInflater.from(requireContext()))
        val customDialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()

        // Set the notification text
        dialogBinding.notificationText.setText(model.NotificationText)

        dialogBinding.ivCancelDialog.setOnClickListener { customDialog.dismiss() }

        // Set up RecyclerView for attachments
        if (model.NoticeFiles != null && model.NoticeFiles.isNotEmpty()) {
            dialogBinding.noItems.visibility = View.GONE
            dialogBinding.filesRecyclerView.adapter = RecyclerViewFileAdapter(model.NoticeFiles, this)
        } else {
            dialogBinding.noItems.visibility = View.VISIBLE
        }

        // Show the dialog
        customDialog.show()
    }

    override fun onItemClick(model: NoticeFilesModel, position: Int) {
        try {
            Log.d("url","url is :${model.FileURL}")
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(model.FileURL)))
        }catch (exc:Exception){
            Log.d("url","url is :${exc}")
        }
    }
}

