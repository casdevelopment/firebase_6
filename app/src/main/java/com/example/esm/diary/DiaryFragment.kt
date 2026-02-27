package com.example.esm.diary

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.esm.BuildConfig
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentDiaryBinding
import com.example.esm.diary.adapters.DiaryAdapter
import com.example.esm.diary.models.DiaryModel
import com.example.esm.diary.viewmodels.DiaryViewModel
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DiaryFragment : Fragment(), DiaryAdapter.onDownloadClickListener {

    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: DiaryViewModel by viewModel()
    var diaryDate: String = ""
    lateinit var calendar: Calendar
    var permissionCode = 101

    val args : DiaryFragmentArgs by navArgs()


    lateinit var binding: FragmentDiaryBinding
    var stdDiaryList: ArrayList<DiaryModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_diary,
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

        if(requireContext().packageName.equals("com.bag.esm")){
            if (args.diaryType == 1) {
                binding.tvTitle.text = "ClassWork"
                binding.showResult.text = "Show ClassWork"
                binding.noItem.text = "No classWork found"
            } else if (args.diaryType == 2) {
                binding.tvTitle.text = "Homework"
                binding.showResult.text = "Show HomeWork"
                binding.noItem.text = "No HomeWork found"
            }
        }  else {
            binding.tvTitle.text = "Daily Diary"
            binding.showResult.text = "Show Diary"
            binding.noItem.text = "No diaries found"
        }



        calendar = Calendar.getInstance()
        initView()
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()
    }

    private fun initView() {
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
            binding.diaryRecyclerView.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE
        } else {
            defaultSetdate()
        }
        binding.date.setOnClickListener {
            setDateOfCalendar()
        }
        binding.showResult.setOnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
                binding.diaryRecyclerView.visibility= View.GONE
                binding.noItem.visibility= View.VISIBLE
            } else {
                setDataForApi()
            }
        }
    }

    private fun defaultSetdate() {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.date.setText(sdf.format(calendar!!.time))
        diaryDate = sdf.format(calendar!!.time)
        setDataForApi()
        Log.d("", "")


    }

    private fun setDateOfCalendar() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DialogTheme,
            { view, year, monthOfyear, dayOfMonth ->
                val dat = (year.toString() + "-" + (monthOfyear + 1) + "-" + dayOfMonth.toString())
                diaryDate = dat
                binding.date.setText(diaryDate)

            },
            year,
            month,
            day

        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(requireActivity().getColor(R.color.colorAccent))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(requireActivity().getColor(R.color.colorAccent))


    }

    private fun setDataForApi() {
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = 0
        // identityModel.DiaryDate = "2023-12-06"
        identityModel.DiaryDate = diaryDate
        identityModel.DiaryType = args.diaryType
        callGetStudentDiary(identityModel)
        Log.d("model", "${identityModel.DiaryDate}")


    }

    private fun callGetStudentDiary(model: IdentityModel) {
        viewModel.getStudentDiary(model).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(requireActivity())
                    Log.d("response", "loading")

                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.isSuccessful) {
                            Log.d("response", "is succesful")
                            var response = apiResponse.data.body()
                            stdDiaryList.clear()
                            if (response != null) {
                                response.DiaryList?.let { stdDiaryList.addAll(it) }
                                Log.d("response", "diary list: $stdDiaryList")
                                setRecyclerView(stdDiaryList)

                            }else{
                                Toast.makeText(requireContext(), " api response is null", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.d("response", "not succesful")
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }

                Status.ERROR -> {
                    AppUtils.stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                    Log.d("response", "error:${apiResponse.message}")

                }
            }

        }


    }

    private fun setRecyclerView(list: ArrayList<DiaryModel>) {
        if (!list.isEmpty()) {
            binding.diaryRecyclerView.adapter = DiaryAdapter(list, this,requireContext())
            binding.diaryRecyclerView.visibility = View.VISIBLE
            binding.noItem.visibility = View.GONE

        } else {
            binding.diaryRecyclerView.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE


        }

    }

    override fun onItemClick(position: Int, diaryModel: DiaryModel) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            writeResponseBodyToDisk(diaryModel)
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionCode)

            }else{
                writeResponseBodyToDisk(diaryModel)

            }
        }



    }

    private fun writeResponseBodyToDisk(diaryModel: DiaryModel) {
        try {
            /* file_byte is yous json string*/
            val decodestring: ByteArray =
                Base64.decode(diaryModel.LogoContent, Base64.DEFAULT)
            val file =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val dir = File("$file/ESM/Documents/")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val document = File(dir, diaryModel.UserFileName)
            if (document.exists()) {
                document.delete()
            }
            val fos = FileOutputStream(document.absolutePath)
            fos.write(decodestring)
            fos.close()
            Toast.makeText(requireContext(), "Files saved to " + document.absolutePath, Toast.LENGTH_LONG)
                .show()
            val intent = Intent(Intent.ACTION_VIEW) //
                .setDataAndType(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                        requireContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        document
                    ) else Uri.fromFile(document),
                    "*/*"
                ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            requireContext().startActivity(intent)
        } catch (e: Exception) {
            Log.d("response","error: ${e.message}")



        }



    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionCode){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  writeResponseBodyToDisk()


            }
            else{
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), permissionCode
                )
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }*/



}