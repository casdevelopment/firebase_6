package com.example.esm.complaint

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.complaint.adapter.ComplaintAdapterResponse
import com.example.esm.complaint.models.ComplaintResponseList
import com.example.esm.complaint.models.ComplaintResponseModel
import com.example.esm.complaint.viewmodels.ComplaintViewModel
import com.example.esm.databinding.FragmentResponseChatBinding
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.HashMap


class ResponseChatFragment : Fragment() {

    private val viewModel: ComplaintViewModel by viewModel()
    private lateinit var binding: FragmentResponseChatBinding
    val args : ResponseChatFragmentArgs by navArgs()
    private lateinit var chatAdapter : ComplaintAdapterResponse
    private var chatList = ArrayList<ComplaintResponseModel>()
    private lateinit var fields: HashMap<String, Any>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResponseChatBinding.inflate(inflater)
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

        setupAdapter()

        fields = HashMap()

        binding.sendButton.setOnClickListener(View.OnClickListener {
            callApi()
        })
        getChatCallApi()
    }
    private fun setupAdapter() {
        chatAdapter = ComplaintAdapterResponse(chatList,requireContext())
        binding.rclChat.apply {
            this.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true)
            this.adapter = chatAdapter
        }
    }
    private fun callApi() {
        val message = binding.message.text.toString()
        if (message.isNotBlank()) {
            fields["ComplaintIdFk"] = args.complainId
            fields["ComplaintResponseType"] = "Guardian"
            fields["Id"] = 0
            fields["Response"] = message
            fields["UserIdentity"] = args.userIdentity

        //    val model = ComplaintResponseModel(0,args.complainId,message,"Guardian",args.userIdentity)

            viewModel.submitComplaintMessage(fields).observe(viewLifecycleOwner) { serverResponse ->
                when (serverResponse.status) {
                    Status.LOADING -> {
                        AppConstants.startLoader(requireActivity())
                    }
                    Status.SUCCESS -> {
                        AppConstants.stopLoader()
                        if (serverResponse.data != null) {
                            if (serverResponse.data.code() == 200) {
                                //   showToast(serverResponse.data.body()!!.message)
                              //  chatAdapter.notifyItemInserted(0)
                                val message = fields["Response"] ?: ""
                                chatList.add(0, ComplaintResponseModel(0, args.complainId,message.toString(), "","",1))
                                chatAdapter.notifyItemInserted(0)
                                binding.message.setText("")
                                binding.rclChat.scrollToPosition(0)
                                // chatList.add(0, model)
                               // chatAdapter.notifyItemInserted(0)
                                getChatCallApi()
                            } else {
                                Toast.makeText(requireContext(), AppConstants.setApiErrorResponse(serverResponse.data.errorBody()), Toast.LENGTH_SHORT).show()
                                Log.v("ad","success " + AppConstants.setApiErrorResponse(serverResponse.data.errorBody()))

                            }
                        }
                    }
                    Status.ERROR -> {
                        AppConstants.stopLoader()
                        Toast.makeText(requireContext(), serverResponse.message!!, Toast.LENGTH_SHORT).show()
                        Log.v("ad","ERROR " + serverResponse.message!!)
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Can't Send empty message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getChatCallApi() {
        val model = ComplaintResponseList(args.complainId,args.userIdentity)
        viewModel.getComplaintResponseList(model).observe(viewLifecycleOwner) { serverResponse ->
                when (serverResponse.status) {
                    Status.LOADING -> {
                        AppConstants.startLoader(requireActivity())
                    }
                    Status.SUCCESS -> {
                        AppConstants.stopLoader()
                        if (serverResponse.data != null) {
                            if (serverResponse.data.code() == 200) {
                                val chatMessages = serverResponse.data.body()!!.ComplaintResponse?.filterNotNull() ?: emptyList()
                                chatList.clear()
                                chatList.addAll(chatMessages)
                                chatAdapter.notifyDataSetChanged()

                            } else {
                             Toast.makeText(requireContext(), AppConstants.setApiErrorResponse(serverResponse.data.errorBody()), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Status.ERROR -> {
                        AppConstants.stopLoader()
                        Toast.makeText(requireContext(), serverResponse.message!!, Toast.LENGTH_SHORT).show()

                    }
                }
            }
    }
}

