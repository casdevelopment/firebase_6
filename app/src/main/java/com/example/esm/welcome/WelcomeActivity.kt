package com.example.esm.welcome

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.ActivityWelcomeBinding
import com.example.esm.network.Status
import com.example.esm.splash.SplashActivity
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.adapters.WelcomeAdapter
import com.example.esm.welcome.models.DataHolder
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentDataModel
import com.example.esm.welcome.viewmodels.WelcomeViewModel
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeActivity : AppCompatActivity(), WelcomeAdapter.onItemClickListener {
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: WelcomeViewModel by viewModel()
    var studentList: ArrayList<StudentDataModel> = ArrayList()
    lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()


        if (packageName.equals("com.tafs.esm")) {
            binding.signOut.visibility = View.VISIBLE
            binding.ivLogout.visibility = View.GONE
        } else {
            binding.signOut.visibility = View.GONE
            binding.ivLogout.visibility = View.VISIBLE


        }
    }

    private fun initView() {

        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
            displayDummyData()
        } else {
            saveDataForApi()
        }

        binding.ivLogout.setOnClickListener {
            confirmationPopup()
        }

        binding.signOut.setOnClickListener {
            confirmationPopup()
        }


    }

    private fun setAppColors(primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        // Set the colors for the status bar and action bar
        window.statusBarColor = ContextCompat.getColor(this, primaryDarkColor)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, primaryColor)))
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
    }

    private fun saveDataForApi() {
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = 0
        identityModel.Month = 0
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = 0
        identityModel.Year = 0
        callStudentListApi(identityModel)

    }

    private fun callStudentListApi(model: IdentityModel) {
        viewModel.studentListApi(model).observe(this@WelcomeActivity) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(this@WelcomeActivity)
                    Log.d("response", "loading")

                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.isSuccessful) {
                            // Toast.makeText(this@WelcomeActivity, "issuccefful", Toast.LENGTH_SHORT).show()
                            var response = apiResponse.data.body()
                            response!!.StudentList?.let {
                                studentList.addAll(it)
                                setRecyclerView(studentList)
                            }

                        } else {
                            Toast.makeText(
                                this@WelcomeActivity, " else" +
                                        "error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this@WelcomeActivity, apiResponse.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.d("response", "error:${apiResponse.message}")
                    AppUtils.stopLoader()
                }
            }

        }

    }

    private fun setRecyclerView(studentList: ArrayList<StudentDataModel>) {
        binding.studentListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.studentListRecyclerView.adapter = WelcomeAdapter(studentList, this,this@WelcomeActivity)

        if (studentList.isEmpty()) {
            binding.studentListRecyclerView.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE
        } else {
            binding.studentListRecyclerView.visibility = View.VISIBLE
            binding.noItem.visibility = View.GONE
            notificationData(studentList)
        }

    }

    private fun confirmationPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_)
        builder.setMessage(R.string.logout_confirmation_message)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            logoutUser()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun logoutUser() {
        sharedPrefsHelper.logout()
        val intent = Intent(this@WelcomeActivity, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    override fun onItemClick(studentDataModel: StudentDataModel, position: Int) {
        Log.d("welcomeactivity", "student data: ${studentDataModel.SchoolName}")
        Log.d("welcomeactivity", "student data: ${studentDataModel.ClassName}")
        Log.d("welcomeactivity", "student data: ${studentDataModel.SchoolName}")
        // Log.d("welcomeactivity", "student data: ${studentDataModel.StudentPictureString}")
        Log.d("welcomeactivity", "student data: ${studentDataModel.SchoolLogoString}")

        AppConstants.STUDENT_NAME = studentDataModel.StudentName.toString()
        AppConstants.CLASS_NAME = studentDataModel.ClassName +" "+ studentDataModel.SectionName
        AppConstants.SCHOOL_NAME = studentDataModel.SchoolName.toString()
        AppConstants.STUDENT_ID = studentDataModel.StudentId ?: 0
        AppConstants.STUDENT_SCHOOL_LOGO = studentDataModel.SchoolLogoString.toString()
        // AppConstants.STUDENT_PICTURE_STRING = studentDataModel.StudentPictureString.toString()
        /// studentDataModel.StudentPictureString?.let { sharedPrefsHelper.setStudentPictureString(it) }
        Log.d("studentModel", "student data: $studentDataModel")
        if(AppUtils.checkConnectivity(this@WelcomeActivity)){

            DataHolder.studentModel = studentDataModel
            var intent = Intent(this@WelcomeActivity, DashboardActivity::class.java)
     /*       val bundle = Bundle()
            bundle.putParcelable("studentDataModel", studentDataModel)
            intent.putExtras(bundle)*/
            startActivity(intent)
        } else {
            Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun displayDummyData() {
        Log.d("displayDummyData", "displayDummyData ")
        if (packageName.equals("com.fssa.esm")){
            val studentList = parseJson(AppConstants.DummyJsonResponseFssa)
            setRecyclerView(studentList)
        }else  if (packageName.equals("com.kpsi.esm")){
            val studentList = parseJson(AppConstants.DummyJsonResponseKPSI)
            setRecyclerView(studentList)
        }else  if (packageName.equals("com.rha.esm")){
            val studentList = parseJson(AppConstants.DummyJsonResponseRha)
            setRecyclerView(studentList)
        }else  if (packageName.equals("com.edukala.esm")){
            val studentList = parseJson(AppConstants.DummyJsonResponseEduKala)
            setRecyclerView(studentList)
        }
    }




    private fun parseJson(json: String): ArrayList<StudentDataModel> {
        val studentList: ArrayList<StudentDataModel> = java.util.ArrayList()
        try {
            val jsonObject = JSONObject(json)
            val studentArray = jsonObject.getJSONArray("StudentList")
            for (i in 0 until studentArray.length()) {
                val studentObject = studentArray.getJSONObject(i)
                val student = StudentDataModel()
                student.StudentId = studentObject.getInt("StudentId")
                student.StudentName = studentObject.getString("StudentName")
                student.FatherName = studentObject.getString("FatherName")
                student.AdmissionNumber = studentObject.getString("AdmissionNumber")
                student.ClassName = studentObject.getString("ClassName")
                student.SectionName = studentObject.getString("SectionName")
                student.SchoolName = studentObject.getString("SchoolName")
                student.SchoolLogoString = studentObject.getString("SchoolLogoString")

                if (studentObject.has("StudentPictureString")) {
                    student.StudentPictureString = studentObject.getString("StudentPictureString")
                }
                studentList.add(student)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return studentList
    }

    private fun notificationData(students: List<StudentDataModel>) {
        Log.v("notificationData", "STUDENT_ID- " + AppConstants.NOTIFICATION_STD_ID)
        Log.v("notificationData", "BUTTON_TYPE- " + AppConstants.BUTTON_TYPE)
        if (AppConstants.NOTIFICATION_STD_ID != null && AppConstants.BUTTON_TYPE != null) {
            for (i in students.indices) {
                if (AppConstants.NOTIFICATION_STD_ID ==  students[i].StudentId.toString()) {
                    AppConstants.STUDENT_NAME = students[i].StudentName.toString()
                    AppConstants.CLASS_NAME = students[i].ClassName + students[i].SectionName
                    AppConstants.SCHOOL_NAME = students[i].SchoolName.toString()
                    AppConstants.STUDENT_ID = students[i].StudentId ?: 0
                    AppConstants.STUDENT_SCHOOL_LOGO = students[i].SchoolLogoString.toString()

                    if(AppUtils.checkConnectivity(this@WelcomeActivity)){

                        DataHolder.studentModel = students[i]
                        val intent = Intent(this@WelcomeActivity, DashboardActivity::class.java)

                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}