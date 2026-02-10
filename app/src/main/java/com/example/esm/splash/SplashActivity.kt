package com.example.esm.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.esm.R
import com.example.esm.databinding.ActivitySplashBassBinding
import com.example.esm.databinding.ActivitySplashBinding
import com.example.esm.databinding.ActivitySplashFssaBinding
import com.example.esm.databinding.ActivitySplashKpsiBinding
import com.example.esm.login.LoginActivity
import com.example.esm.utils.AppConstants
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.WelcomeActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var appUpdateManager: AppUpdateManager? = null
    private var FLEXIBLE_APP_UPDATE_REQ_CODE = 123
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    private val sharePreferenceHelper: SharedPrefsHelper by inject()
    val sharedPrefsHelper: SharedPrefsHelper by inject()

    lateinit var binding: ActivitySplashBinding


    lateinit var bindingBass : ActivitySplashBassBinding
    lateinit var bindingKpsi : ActivitySplashKpsiBinding
    lateinit var bindingFssa: ActivitySplashFssaBinding

  //  var sisaDelay:Long=14000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //disable night mode fpr your app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivitySplashBinding.inflate(layoutInflater)


        bindingBass = ActivitySplashBassBinding.inflate(layoutInflater)
        bindingKpsi = ActivitySplashKpsiBinding.inflate(layoutInflater)
        bindingFssa = ActivitySplashFssaBinding.inflate(layoutInflater)




        //setContentView(binding
        setView()
        getIntentData()
        getFirebaseToken()
    }

    private fun setView() {
        AppConstants.MOBILE_CODE = resources.getString(R.string.mobile_code).toInt()
        Log.v("FLAVOR", "FLAVOR =" + packageName)
       if (packageName.equals("com.bass.esm")) {
            setContentView(bindingBass.root)
        }
       else if (packageName.equals("com.kpsi.esm")) {
            setContentView(bindingKpsi.root)
        }
       else if (packageName.equals("com.fssa.esm")) {
            setContentView(bindingFssa.root)
        }
    }

    private fun setAppColors(primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        // Set the colors for the status bar and action bar
        window.statusBarColor = ContextCompat.getColor(this, primaryDarkColor)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, primaryColor)))
    }

    private fun getIntentData() {
        AppConstants.BUTTON_TYPE = intent.getStringExtra("buttonType").toString()
        AppConstants.NOTIFICATION_STD_ID = (intent.extras?.getString("studentId").toString())
        AppConstants.POSITION = intent.getStringExtra("position").toString()
        AppConstants.N_TITLE = intent.getStringExtra("title").toString()
        AppConstants.N_BODY = intent.getStringExtra("body").toString()

        Log.d("notificationData", "button  type is : ${AppConstants.BUTTON_TYPE}")
        Log.d("notificationData", "student id is : ${AppConstants.NOTIFICATION_STD_ID}")
        Log.d("notificationData", "position id is : ${AppConstants.POSITION}")
        checkForUpdate()
    }

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        installStateUpdatedListener = InstallStateUpdatedListener { state: InstallState ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener()
            }
        }
        checkUpdate()
    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else {
                go()
            }
        }
        appUpdateInfoTask.addOnFailureListener { e: Exception? -> go() }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                FLEXIBLE_APP_UPDATE_REQ_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager!!.unregisterListener(installStateUpdatedListener!!)
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            View(this@SplashActivity),
            "New app is ready!",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Install") { view: View? ->
                if (appUpdateManager != null) {
                    appUpdateManager!!.completeUpdate()
                }
            }
            .setActionTextColor(resources.getColor(R.color.colorPrimary))
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            when (resultCode) {
                RESULT_CANCELED -> {
                    Toast.makeText(
                        applicationContext,
                        "Update canceled by user! ",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    go()
                }

                RESULT_OK -> {
                    go()
                }

                else -> {
                    Toast.makeText(applicationContext, "Update Failed! ", Toast.LENGTH_LONG).show()
                    checkUpdate()
                }
            }
        }
    }


    private fun go() {
        lifecycleScope.launch {
            if (packageName.equals("com.fmr.esm")) {
                delay(2000)
            }
            else{
                delay(2000)
            }
            Log.d("SessionData", "Session.isLoggedIn " + sharePreferenceHelper.isLoggedIn())
            lateinit var intent: Intent
            if (sharedPrefsHelper.isLoggedIn()) {
                intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
            } else {
                    intent = Intent(this@SplashActivity, LoginActivity::class.java)

            }
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }
    }

    private fun switchActivity() {
        if (sharedPrefsHelper.isLoggedIn()) {
            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)

        }
    }

    private fun getFirebaseToken() {
        com.google.firebase.messaging.FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            sharedPrefsHelper.setFCMToken(token)
            Log.d("SplashActivity", "fcm_token success: $token")
        }.addOnFailureListener {
            Log.d("SplashActivity", "fcm_token failure:")
        }
    }

}