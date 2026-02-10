package com.example.esm.dashboardactivity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.esm.R
import com.example.esm.dashboardfragment.DashboardFragmentDirections
import com.example.esm.dashboardfragment.viewmodels.DashboardFragmentViewModel
import com.example.esm.databinding.ActivityDashboardBinding
import com.example.esm.network.Status
import com.example.esm.splash.SplashActivity
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.AppUtils.checkConnectivity
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.DataHolder
import com.example.esm.welcome.models.StudentDataModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DashboardActivity : AppCompatActivity() {
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    public lateinit var binding: ActivityDashboardBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    lateinit var studentModel: StudentDataModel
    var navController: NavController? = null
    var toolbar: Toolbar? = null
    private val viewModel: DashboardFragmentViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.actionbarLayout.backArrow.visibility = View.GONE
        binding.actionbarLayout.hamburgIcon.visibility = View.VISIBLE
        binding.actionbarLayout.hamburgIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
            setUpNavigation()
        }

        binding.facebook.setOnClickListener(View.OnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                goToLink("https://www.facebook.com/cyberadvancesolutions/")
            } else {
                studentModel.Facebook?.let { it1 -> goToLink(it1) }
            }
        })
        binding.insta.setOnClickListener(View.OnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                goToLink("https://www.instagram.com/cyberadvancesolution/")
            } else {
                studentModel.Instagram?.let { it1 -> goToLink(it1) }
            }
        })

        binding.twitter.setOnClickListener(View.OnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                goToLink("https://x.com/?lang=en")
            } else {
                studentModel.Twitter?.let { it1 -> goToLink(it1) }
            }
        })

        binding.youtube.setOnClickListener(View.OnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                goToLink("https://www.youtube.com/watch?v=KwkG2FThfWA&ab_channel=CyberAdvanceSolutions%28PVT%29LTD.")
            } else {
                studentModel.Youtube?.let { it1 -> goToLink(it1) }
            }
        })

        binding.linkdin.setOnClickListener(View.OnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                goToLink("https://www.linkedin.com/company/cyberasolutions/mycompany/")
            } else {
                studentModel.Linkedin?.let { it1 -> goToLink(it1) }
            }
        })

        binding.website.setOnClickListener(View.OnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                goToLink("https://cyberasol.com/")
            } else {
                studentModel.Websiteurl?.let { it1 -> goToLink(it1) }
            }
        })

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)
        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // actionBarToggle.syncState()
        // Call findViewById on the NavigationView
        navView = findViewById(R.id.nav_view)


        navController = findNavController(this, R.id.nav_host_fragment)

        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission()
            }
        }


        /*  val bundle = intent.extras
          if (bundle != null) {
              studentModel = bundle.getParcelable("studentDataModel")!!
          }*/

        studentModel =
            DataHolder.studentModel ?: throw IllegalStateException("Student data is missing")

        setStudentPersonalInfo(studentModel)

        binding.actionbarLayout.backArrow.setOnClickListener {
            onBackPressed()
        }

        if (packageName.equals("com.qadimslumiere.esm")) {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                binding.alertSmsMenu.visibility = View.GONE
                binding.complaintMenu.visibility = View.GONE
                binding.resultMenu.visibility = View.GONE
                binding.diaryMenu.visibility = View.GONE
                binding.notificationsMenu.visibility = View.GONE
                binding.eventsMenu.visibility = View.GONE
                binding.updatePassword.visibility = View.GONE

            }
        }

        if (packageName.equals("com.iqraeducationsystem.esm")) {
            binding.paymentHistoryMenu.visibility = View.GONE
            binding.feecardMenu.visibility = View.GONE
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                binding.paymentHistoryMenu.visibility = View.GONE
                binding.feecardMenu.visibility = View.GONE
                binding.complaintMenu.visibility = View.GONE
                binding.resultMenu.visibility = View.GONE
                binding.diaryMenu.visibility = View.GONE
                binding.notificationsMenu.visibility = View.GONE
                binding.eventsMenu.visibility = View.GONE
                binding.updatePassword.visibility = View.GONE

            }
        }

        if (packageName.equals("com.islamicintpublicsclandclg.esm")) {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                binding.alertSmsMenu.visibility = View.GONE
                binding.complaintMenu.visibility = View.GONE
                binding.resultMenu.visibility = View.GONE
                binding.diaryMenu.visibility = View.GONE
                binding.notificationsMenu.visibility = View.GONE
                binding.eventsMenu.visibility = View.GONE
                binding.updatePassword.visibility = View.GONE

            }
        }

        if (packageName.equals("com.starschoolmandifaizabad.esm")) {
            binding.topConstraintMargin.setPadding(0,0,0,0)
            binding.bAppBar.setBackgroundColor(getColor(R.color.colorPrimary))
            binding.linkdin.setColorFilter(
                ContextCompat.getColor(this, R.color.colorWhite),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.website.setColorFilter(
                ContextCompat.getColor(this, R.color.colorWhite),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                binding.alertSmsMenu.visibility = View.GONE
                binding.complaintMenu.visibility = View.GONE
                binding.resultMenu.visibility = View.GONE
                binding.diaryMenu.visibility = View.GONE
                binding.notificationsMenu.visibility = View.GONE
                binding.eventsMenu.visibility = View.GONE
                binding.updatePassword.visibility = View.GONE
            }
        }

        if (packageName.equals("com.ssmsch.esm")) {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                binding.alertSmsMenu.visibility = View.GONE
                binding.complaintMenu.visibility = View.GONE
                binding.resultMenu.visibility = View.GONE
                binding.diaryMenu.visibility = View.GONE
                binding.notificationsMenu.visibility = View.GONE
                binding.eventsMenu.visibility = View.GONE
                binding.updatePassword.visibility = View.GONE

            }
        }


        if (packageName.equals("com.rubricseducationsystem.esm")) {
            binding.updatePasswordMenu.visibility = View.VISIBLE
        } else {
            binding.updatePasswordMenu.visibility = View.GONE
        }

        if (packageName.equals("com.daswahdatbranch.esm")) {
            binding.updatePassword.visibility = View.VISIBLE
        }  else {
            binding.updatePassword.visibility = View.GONE
        }

        if (packageName.equals("com.csm.esm")) {
            binding.diaryMenu.visibility = View.GONE
        }


        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
            binding.updatePasswordMenu.visibility = View.GONE
            binding.updatePassword.visibility = View.GONE
        }

        if (packageName.equals("com.bass.esm")){
            binding.twitter.visibility = View.GONE
            binding.linkdin.visibility = View.GONE
        }
        fireBaseNotificationData()
        hideLayout()
    }

    private fun setAppColors(primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        // Set the colors for the status bar and action bar
        window.statusBarColor = ContextCompat.getColor(this, primaryDarkColor)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    primaryColor
                )
            )
        )

        binding.profilePart.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
        binding.actionbarLayout.rlBg.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
        binding.navView.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
        binding.llNavView.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
    }


    private fun setStudentPersonalInfo(studentModel: StudentDataModel) {

        with(studentModel) {
            with(binding) {
                //        set School logo
                AppUtils.loadImageImage(
                    SchoolLogoString,
                    imageViewSchool,
                    this@DashboardActivity,
                    R.drawable.placeholder,
                    R.drawable.placeholder
                )

                //        set user logo
                AppUtils.loadImageImage(
                    StudentPictureString,
                    studentImage,
                    this@DashboardActivity,
                    R.drawable.avtr,
                    R.drawable.avtr
                )


                //set school logo on navigation view

                AppUtils.loadImageImage(
                    SchoolLogoString,
                    imageViewSchoolNavi,
                    this@DashboardActivity,
                    R.drawable.placeholder,
                    R.drawable.placeholder
                )

                Log.v("studentClass","studentClass "+AppConstants.CLASS_NAME)
                studentName.text = AppConstants.STUDENT_NAME
                studentClass.text = AppConstants.CLASS_NAME
                schoolName.text = AppConstants.SCHOOL_NAME
                schoolNameNavi.text = AppConstants.SCHOOL_NAME
            }

        }


    }


    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
        //return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        super.onBackPressed()
    }


    fun backArrowVisible() {
        binding.actionbarLayout.backArrow.visibility = View.VISIBLE

    }

    fun backArrowInvisible() {
        binding.actionbarLayout.backArrow.visibility = View.GONE

    }

    fun drawIconInvisible() {
        binding.actionbarLayout.hamburgIcon.visibility = View.GONE
    }

    fun drawIconVisible() {
        binding.actionbarLayout.hamburgIcon.visibility = View.VISIBLE
    }

    private fun setUpNavigation() {

        binding.paymentHistoryMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_paymentHistoryFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.attendanceMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_attendanceFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.resultMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_resultsFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.alertSmsMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_alertSMSFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.notificationsMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
              //  goToNotification()
                goToFragment(R.id.action_dashboardFragment_to_noticeFragment, true)

            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.feecardMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_feeCardFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
        binding.eventsMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_eventsCalendarFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.complaintMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                goToFragment(R.id.action_dashboardFragment_to_complaintFragment, true)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.diaryMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                //   goToFragment(R.id.action_dashboardFragment_to_diaryFragment, true)
                val action = DashboardFragmentDirections.actionDashboardFragmentToDiaryFragment(1)
                navController?.navigate(action)
                if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }


            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.logoutMenu.setOnClickListener {
            if (checkConnectivity(this@DashboardActivity)) {
                sharedPrefsHelper.logout()
                val intent = Intent(this@DashboardActivity, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
        binding.updatePasswordMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            if (checkConnectivity(this@DashboardActivity)) {
                updatePasswordDialog()
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

        binding.updatePassword.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            if (checkConnectivity(this@DashboardActivity)) {
                updatePasswordDialog()
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Internet is not available",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }


    }

    fun goToLink(link: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        } catch (ex: Exception) {
            Toast.makeText(
                this,
                "Url $link is invalid or no application found for this url to open",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    public fun goToNotification() {
//         val intent = Intent(this, NoticeActivity::class.java)
//         startActivity(intent)
    }

    private fun goToFragment(id: Int, isMenu: Boolean) {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        }
        if (isMenu) {
            Handler().postDelayed({ navController?.navigate(id) }, 0)
        } else {
            navController?.navigate(id)
        }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun updatePasswordDialog() {

        val fields: HashMap<String, String> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["isValid"] = "0"


        Log.v("showRunning", "UserIdentity  " + sharedPrefsHelper.getUserId().toString())
        val dialog = Dialog(this@DashboardActivity)
        if(packageName.equals("com.rubricseducationsystem.esm")) {
            dialog.setContentView(R.layout.dialog_update)
        } else {
            dialog.setContentView(R.layout.update_password_dialog)
        }
        dialog.setCancelable(true)
        val updateButton = dialog.findViewById<AppCompatButton>(R.id.updateButton)
        val etOldPassword = dialog.findViewById<TextInputEditText>(R.id.etOldPassword)
        val etNewPassword = dialog.findViewById<TextInputEditText>(R.id.etNewPassword)
        val oldPasswordError = dialog.findViewById<TextView>(R.id.oldPasswordError)
        val newPasswordError = dialog.findViewById<TextView>(R.id.newPasswordError)
        val cancel = dialog.findViewById<AppCompatImageView>(R.id.ivCancelDialog)


        updateButton.setOnClickListener {

            if (etOldPassword.text.toString().isEmpty()) {
                oldPasswordError.visibility = View.VISIBLE
                oldPasswordError.setText(R.string.field_required)
                return@setOnClickListener
            }
            if (etNewPassword.text.toString().isEmpty()) {
                newPasswordError.visibility = View.VISIBLE
                newPasswordError.setText(R.string.field_required)
                return@setOnClickListener
            }
            fields["OldPassword"] = etOldPassword.text.toString()
            fields["Password"] = etNewPassword.text.toString()
            callUpdatePasswordApi(fields)

            dialog.dismiss()

        }
        cancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
        val window = dialog.window!!
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun callUpdatePasswordApi(fields: HashMap<String, String>) {
        Log.v("showRunning", "callUpdatePasswordApi  " )
        viewModel.updatePassword(fields).observe(this@DashboardActivity) { apiResponse ->
            when (apiResponse.status) {
                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.code() == 200) {
                            Toast.makeText(
                                this@DashboardActivity,
                                "Update Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            sharedPrefsHelper.logout()
                            val intent = Intent(this@DashboardActivity, SplashActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@DashboardActivity,
                                "Current Password Is Incorrect",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }



                }

                Status.ERROR -> {
                    AppUtils.stopLoader()
                }

                Status.LOADING -> {
                    AppUtils.startLoader(this@DashboardActivity)
                }
            }
        }
    }

    private fun fireBaseNotificationData() {
        if (AppConstants.BUTTON_TYPE != null && AppConstants.BUTTON_TYPE != null) {
            if (packageName.equals("com.bass.esm")) {
                saveNotificationApi()
            }
            if (AppConstants.BUTTON_TYPE.equals("1")) {
                goToFragment(R.id.action_dashboardFragment_to_paymentHistoryFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("2")) {
                goToFragment(R.id.action_dashboardFragment_to_attendanceFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("3")) {
                goToFragment(R.id.action_dashboardFragment_to_resultsFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("4")) {
                goToFragment(R.id.action_dashboardFragment_to_alertSMSFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("5")) {
                goToFragment(R.id.action_dashboardFragment_to_noticeFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("6")) {
                goToFragment(R.id.action_dashboardFragment_to_feeCardFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("7")) {
                goToFragment(R.id.action_dashboardFragment_to_eventsCalendarFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("8")) {
                goToFragment(R.id.action_dashboardFragment_to_complaintFragment, true)
            } else if (AppConstants.BUTTON_TYPE.equals("9")) {
                val action = DashboardFragmentDirections.actionDashboardFragmentToDiaryFragment(1)
                navController?.navigate(action)
                if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
            }
            AppConstants.NOTIFICATION_STD_ID = ""
            AppConstants.NOTIFICATION_STD_ID = ""
        }
    }

    fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1234
                )
            }
        } catch (e: java.lang.Exception) {
        }
    }

    fun saveNotificationApi() {
        val fields = HashMap<String, String>()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["Body"] = AppConstants.N_BODY
        fields["Title"] = AppConstants.N_TITLE
        fields["StudentID"] =  AppConstants.NOTIFICATION_STD_ID
        fields["ButtonType"] =  AppConstants.NOTIFICATION_STD_ID
        fields["Position"] =  AppConstants.POSITION

        viewModel.saveNotificationApi(fields)
    }

    fun hideLayout(){
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
            binding.alertSmsMenu.visibility = View.GONE
            binding.complaintMenu.visibility = View.GONE
            binding.resultMenu.visibility = View.GONE
            binding.diaryMenu.visibility = View.GONE
            binding.notificationsMenu.visibility = View.GONE
            binding.eventsMenu.visibility = View.GONE
            binding.updatePassword.visibility = View.GONE
        }
    }
}