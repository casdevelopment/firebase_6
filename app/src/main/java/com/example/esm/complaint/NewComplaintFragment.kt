package com.example.esm.complaint

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class NewComplaintFragment : Fragment() {
    lateinit var binding: FragmentNewComplaintBinding
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: ComplaintViewModel by viewModel()
    private val activity: DashboardActivity? = null

    private var selectedFileUri: Uri? = null

    // 📂 File Picker (Image + PDF)
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {

                if (isValidFile(it)) {

                    selectedFileUri = it

                    // ✅ UPDATE TEXT VIEW HERE
                    binding.fileName.text = getFileName(it)

                    Toast.makeText(requireContext(), "File selected", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Only Image or PDF allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_complaint,
            container,
            false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.attachFile.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }
        if (requireContext().packageName.equals("com.rha.esm")) {
            binding.attachFile.visibility = VISIBLE
            binding.fileName.visibility = VISIBLE
        }


        binding.submitComplaint.setOnClickListener {
            if (validation()) {
                if (requireContext().packageName.equals("com.rha.esm")) {
                    callComplaintRegistrationApiForRHSClient()
                } else {
                    val complaintModel = ComplaintModel()

                    complaintModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
                    complaintModel.ComplaintTitle = binding.titleComplaint.text.toString()
                    complaintModel.ComplaintText = binding.complaintText.text.toString()
                    complaintModel.StudentId = AppConstants.STUDENT_ID.toString()

                    complaintModel.ComplaintId = 0
                    complaintModel.Logged = 0
                    callComplaintRegistrationApi(complaintModel)
                }


            }

        }

    }


    private fun validation(): Boolean {

        if (binding.titleComplaint.text.toString().isEmpty()) {
            binding.titleError.visibility = View.VISIBLE
            binding.titleError.text = "Please enter title for complaint"
            return false
        } else if (binding.complaintText.text.isEmpty()) {
            binding.complaintError.visibility = View.VISIBLE
            binding.complaintError.text = "Please enter complaint details"
            return false
        } else if (selectedFileUri == null) {
            Toast.makeText(requireContext(), "Please attach a file", Toast.LENGTH_SHORT).show()
            return false
        } else {
            binding.titleError.visibility = View.GONE
            binding.complaintError.visibility = View.GONE
            return true
        }
    }

    private fun isValidFile(uri: Uri): Boolean {
        val type = requireContext().contentResolver.getType(uri)
        return type == "application/pdf" || type?.startsWith("image/") == true
    }

    //  Convert URI → File
    private fun uriToFile(uri: Uri): File {
        val file = File(requireContext().cacheDir, "upload_${System.currentTimeMillis()}")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = file.outputStream()

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        return file
    }


    private fun callComplaintRegistrationApiForRHSClient() {

        val complaintModel = ComplaintModel().apply {

            UserIdentity = sharedPrefsHelper.getUserId().toString()
            ComplaintTitle = binding.titleComplaint.text.toString()
            ComplaintText = binding.complaintText.text.toString()
            StudentId = AppConstants.STUDENT_ID.toString()

//            ComplaintId = 0
//            Logged = 0
//

        }


        // 2️⃣ Convert model → JSON → RequestBody
        val json = Gson().toJson(complaintModel)
        val modelBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        // 3️⃣ Prepare file (optional)
        var filePart: MultipartBody.Part? = null

        selectedFileUri?.let { uri ->
            val file = uriToFile(uri)
            val mimeType = requireContext().contentResolver.getType(uri)
                ?: "application/octet-stream"

            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())

            filePart = MultipartBody.Part.createFormData(
                "ComplaintAttachment",
                file.name,
                requestFile
            )
        }



        viewModel.complaintRegistrationForRHSClient(modelBody, filePart)
            .observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse.status) {
                    Status.LOADING -> {
                        AppUtils.startLoader(requireActivity())
                        Log.d("response", "loading")

                    }

                    Status.SUCCESS -> {
                        AppUtils.stopLoader()
                        if (apiResponse.data != null) {
                            if (apiResponse.data.isSuccessful) {
                                findNavController().popBackStack()
                                // supportFragmentManager.popBackStack("YOUR_FRAGMENT_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                // getActivity()?.finish()
                                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                            Log.d("response"," new complaint is succesful")
//                            getFragmentManager()?.popBackStack();

                            }
                        }
                    }

                    Status.ERROR -> {
                        AppUtils.stopLoader()
                        Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("response", "error:${apiResponse.message}")

                    }
                }

            }


    }
    private fun callComplaintRegistrationApi(complaintModel: ComplaintModel) {


        viewModel.complaintRegistration(complaintModel)
            .observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse.status) {
                    Status.LOADING -> {
                        AppUtils.startLoader(requireActivity())
                        Log.d("response", "loading")

                    }

                    Status.SUCCESS -> {
                        AppUtils.stopLoader()
                        if (apiResponse.data != null) {
                            if (apiResponse.data.isSuccessful) {
                                findNavController().popBackStack()
                                // supportFragmentManager.popBackStack("YOUR_FRAGMENT_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                // getActivity()?.finish()
                                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                            Log.d("response"," new complaint is succesful")
//                            getFragmentManager()?.popBackStack();

                            }
                        }
                    }

                    Status.ERROR -> {
                        AppUtils.stopLoader()
                        Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("response", "error:${apiResponse.message}")

                    }
                }

            }


    }

    private fun getFileName(uri: Uri): String {
        var name = "Selected File"

        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            val index = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && index >= 0) {
                name = it.getString(index)
            }
        }

        return name
    }


}