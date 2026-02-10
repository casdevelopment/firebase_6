package com.example.esm.login

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.esm.R
import com.example.esm.login.models.LoginRequestResponseModel
import com.example.esm.login.viewmodels.LoginViewModel
import com.example.esm.network.Status
import com.example.esm.signup.SignUpActivity
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.WelcomeActivity
import com.google.android.material.button.MaterialButton
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity <T>: AppCompatActivity() {
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: LoginViewModel by viewModel()

    private lateinit var phoneNo : EditText
    private lateinit var mobileCode : EditText
    private lateinit var etPassword : EditText
    private lateinit var proceedButton : MaterialButton
    private lateinit var phoneNoError : TextView
    private lateinit var entityCodeError : TextView
    private lateinit var passwordError : TextView
    private lateinit var tvGoToSingUp : TextView
    private lateinit var app_logo : ImageView


    private lateinit var campusSpinner: Spinner
    private var selectedIndex: Int = -1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setView()
        phoneNo = findViewById(R.id.phoneNo)
        mobileCode = findViewById(R.id.entityCode)
        etPassword = findViewById(R.id.etPassword)
        proceedButton = findViewById(R.id.proceedButton)
        phoneNoError = findViewById(R.id.phoneNoError)
        entityCodeError = findViewById(R.id.entityCodeError)
        passwordError = findViewById(R.id.passwordError)
        tvGoToSingUp = findViewById(R.id.tvGoToSingUp)

        setMobileCode()

        //   setContentView(binding.root)

        Log.v("FLAVOR","base url = "+resources.getString(R.string.base_url))
        initView()


        //setAppColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)

    }

    private fun setMobileCode() {

            mobileCode.setText(AppConstants.MOBILE_CODE.toString())

    }

    private fun setView() {

      if (packageName.equals("com.bass.esm")) {
            setContentView(R.layout.activity_login_bass)
        }else if (packageName.equals("com.kpsi.esm")){
          setContentView(R.layout.activity_login_kpsi)
      }else if (packageName.equals("com.fssa.esm")) {
          setContentView(R.layout.activity_login_fssa)
      }



    }
    private fun setAppColors(primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        // Set the colors for the status bar and action bar
        window.statusBarColor = ContextCompat.getColor(this, primaryDarkColor)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, primaryColor)))

        proceedButton.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
        phoneNoError.setTextColor(ContextCompat.getColor(this, accentColor))
        entityCodeError.setTextColor(ContextCompat.getColor(this, accentColor))
        passwordError.setTextColor(ContextCompat.getColor(this, accentColor))
        tvGoToSingUp.setTextColor(ContextCompat.getColor(this, accentColor))

    }

    private fun initView() {
        proceedButton.setOnClickListener {
            validation()
        }
        tvGoToSingUp.setOnClickListener {
            var intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        phoneNo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(phone: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TextChanged","$phone")
                Log.d("TextChanged","$count")
                if (phone!= null){
                    if (!phone.toString().isEmpty()){
                        phoneNoError.visibility = View.GONE
                    }
                    else{
                        phoneNoError.visibility = View.VISIBLE
                    }
                    if (phone.toString().isEmpty() || phone.toString().length != 12){
                      phoneNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_cancel_black_18, 0)
                        phoneNo.requestFocus()

                    }
                    else if (AppUtils.iszeroCheck(phone.toString())){
                        phoneNo.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_cancel_black_18,
                            0
                        )
                        phoneNo.requestFocus()
                        phoneNoError.visibility = View.VISIBLE
                        phoneNoError.setText("Please start phone number without zero")
                    }
                    else{
                        phoneNoError.visibility= View.GONE
                        phoneNo.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_check_circle_white_18,
                            0
                        )
                    }
                }
                else{
                    phoneNo.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.outline_cancel_black_18,
                        0
                    )
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        mobileCode.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(entityCode: CharSequence?, start: Int, before: Int, count: Int) {
                if (entityCode!= null){
                    entityCodeError.visibility= View.GONE
                    if (entityCode.isEmpty()){
                        mobileCode.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_cancel_black_18,
                            0
                        )
                        mobileCode.requestFocus()
                    }
                    else if (!AppUtils.isEntityCodeValid(entityCode.toString())){
                        mobileCode.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_cancel_black_18,
                            0
                        )
                        mobileCode.requestFocus()
                        entityCodeError.visibility = View.VISIBLE
                        entityCodeError.setText("Please enter minimum 1-digit entity code")
                    }
                    else{
                        mobileCode.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_check_circle_white_18,
                            0
                        )
                    }
                }
                else{
                    mobileCode.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.outline_cancel_black_18,
                        0
                    )
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(password: CharSequence?, start: Int, before: Int, count: Int) {
                if (password!= null){
                    passwordError.visibility = View.GONE
                    if (password.isEmpty()){
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_cancel_black_18,
                            0
                        )
                        etPassword.requestFocus()
                    }
                    else if (!AppUtils.isPasswordValid(password.toString())){
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_cancel_black_18,
                            0
                        )
                        etPassword.requestFocus()
                        passwordError.visibility= View.VISIBLE
                        passwordError.setText("Password should be atleast 8 characters long")
                    }
                    else{
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.outline_check_circle_white_18,
                            0
                        )
                    }
                }
                else{
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.outline_cancel_black_18,
                        0
                    )
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    private fun isValidated(): Boolean {
        if (phoneNo.text.toString().isEmpty()) {
            phoneNoError.visibility = View.VISIBLE
            phoneNoError.setText(R.string.field_required)
            return false
        } /*else if (mobileCode.text.toString().isEmpty()) {
            entityCodeError.visibility = View.VISIBLE
            entityCodeError.setText(R.string.field_required)
            return false
        }*/ else if (etPassword.text.toString().isEmpty()) {
            passwordError.visibility = View.VISIBLE
            passwordError.setText(R.string.field_required)
            return false
        } else if (etPassword.text.toString().length < 8) {
            passwordError.visibility = View.VISIBLE
            passwordError.setText(R.string.password_characters_length)
            return false
        } else {
            phoneNoError.visibility = View.GONE
            entityCodeError.visibility = View.GONE
            passwordError.visibility = View.GONE
            return true
        }
    }

    private fun validation() {
        if (AppUtils.checkConnectivity(this@LoginActivity)) {
            if (isValidated()) {
                val phoneNumber = phoneNo.text.toString()
                val password = etPassword.text.toString()
                val mobileCodeInt = mobileCode.text.toString().toInt()
                //val mobileCodeInt = AppConstants.MOBILE_CODE
                if (phoneNumber == "923001234567" && password == "123456789") {
                    sharedPrefsHelper.setUserId("MmFWGVTWzhCirMi0MTAlrXi9Ex//GQG/")
                    sharedPrefsHelper.putBoolean(KEY_DUMMY_LOGIN,true)
                    var intent= Intent(this@LoginActivity,WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    sharedPrefsHelper.setUserMobileCode(mobileCode.text.toString().toInt())
                    val loginModel = LoginRequestResponseModel()
                    loginModel.FcmToken = sharedPrefsHelper.getFCMToken().toString()
                    loginModel.MessageCode = 0
                    loginModel.MobileCode = mobileCodeInt
                    // loginModel.MobileCode = 1119
                    loginModel.MobileNumber = phoneNumber
                    loginModel.Password = password
                    loginModel.isValid = 0
                    callLoginApi(loginModel)
                }
            }
        } else {
            Toast.makeText(this@LoginActivity, "Internet is not available", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun callLoginApi(model: LoginRequestResponseModel) {
        viewModel.loginApi(model).observe(this@LoginActivity){ apiResponse->
            when(apiResponse.status){
                Status.LOADING->{
                    AppUtils.startLoader(this@LoginActivity)
                    Log.d("response","loading")
                }
                Status.SUCCESS->{
                    AppUtils.stopLoader()
                    if (apiResponse.data!!.code()==200){
                        Log.d("response","success: ${apiResponse.data.body()}")
                        if (apiResponse.data.body()?.isEmpty() == true){
                            Toast.makeText(this@LoginActivity, "Unauthorized User", Toast.LENGTH_SHORT).show()
                        }else{
                            apiResponse.data.body()?.let {
                                sharedPrefsHelper.setUserId(it)
                                sharedPrefsHelper.setFMRURL(AppConstants.FMR_URL)
                            }
                            var intent= Intent(this@LoginActivity,WelcomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "Invalid data", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR->{
                    AppUtils.stopLoader()
                    Toast.makeText(this@LoginActivity, apiResponse.message, Toast.LENGTH_SHORT).show()
                    Log.d("response","Error Code")
                }
            }
        }
    }


}