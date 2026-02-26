package com.example.esm.signup

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
import com.example.esm.network.Status
import com.example.esm.signup.model.SignUpRequestResponseModel
import com.example.esm.signup.viewmodel.SignUpViewModel
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {
    //  lateinit var binding : ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModel()
    //  lateinit var bindingAcm : ActivitySignUpDpscBinding

    private lateinit var phoneNo: EditText
    private lateinit var mobileCode: EditText
    private lateinit var etPassword: EditText
    private lateinit var proceedButton: MaterialButton
    private lateinit var phoneNoError: TextView
    private lateinit var entityCodeError: TextView
    private lateinit var passwordError: TextView
    private lateinit var etConfirmPassword: EditText
    private lateinit var confirmPasswordError: TextView
    private lateinit var app_logo_signUp: ImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivitySignUpBinding.inflate(layoutInflater)
//        bindingAcm = ActivitySignUpDpscBinding.inflate(layoutInflater)
        //  setContentView(binding.root)
        setView()

//        if (packageName.equals("com.ari.esm")) {
//            Log.v("FLAVOR", "FLAVOR = esm_main")
//            setContentView(R.layout.activity_sign_up_ari)
//        }

        phoneNo = findViewById(R.id.phoneNo)
        mobileCode = findViewById(R.id.entityCode)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        proceedButton = findViewById(R.id.proceedButton)
        phoneNoError = findViewById(R.id.phoneNoError)
        entityCodeError = findViewById(R.id.entityCodeError)
        passwordError = findViewById(R.id.passwordError)
        confirmPasswordError = findViewById(R.id.confirmPasswordError)



        setMobileCode()
        initView()


        setAppColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)


    }
    private fun setAppColors(primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        // Set the colors for the status bar and action bar
        window.statusBarColor = ContextCompat.getColor(this, primaryDarkColor)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, primaryColor)))

        proceedButton.setBackgroundColor(ContextCompat.getColor(this, primaryColor))
        phoneNoError.setTextColor(ContextCompat.getColor(this, accentColor))
        entityCodeError.setTextColor(ContextCompat.getColor(this, accentColor))
        passwordError.setTextColor(ContextCompat.getColor(this, accentColor))
        confirmPasswordError.setTextColor(ContextCompat.getColor(this, accentColor))

    }

    private fun setView() {
       if (packageName.equals("com.bass.esm")) {
           setContentView(R.layout.activity_sign_up_bass)
       }else if (packageName.equals("com.kpsi.esm")){
           setContentView(R.layout.activity_sign_up_kpsi)
       }else if (packageName.equals("com.fssa.esm")) {
           setContentView(R.layout.activity_sign_up_fssa)
       }else if (packageName.equals("com.rha.esm")) {
           setContentView(R.layout.activity_sign_up_rha)
       }else if (packageName.equals("com.edukala.esm")) {
           setContentView(R.layout.activity_sign_up_edukala)
       }
    }
    private fun setMobileCode() {

            mobileCode.setText(AppConstants.MOBILE_CODE.toString())


    }

    private fun initView() {
        proceedButton.setOnClickListener {
            validation()
        }

        phoneNo.addTextChangedListener(object : TextWatcher {
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

                    if (  packageName.equals("com.rha.esm") && phone.toString().length < 13){
                        phoneNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_cancel_black_18, 0)
                        phoneNo.requestFocus()
                        phoneNoError.visibility = View.VISIBLE


                    }else{
                        phoneNoError.visibility= View.GONE
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
        mobileCode.addTextChangedListener(object : TextWatcher {
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
        etPassword.addTextChangedListener(object : TextWatcher {
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

    private fun validation() {
        if (AppUtils.checkConnectivity(this@SignUpActivity)) {
            if (isValidated()) {
              //  Toast.makeText(this@SignUpActivity, "signup", Toast.LENGTH_SHORT).show()
                val signupModel = SignUpRequestResponseModel()
                signupModel.MobileNumber = phoneNo.text.toString()
              //  signupModel.MobileCode = AppConstants.MOBILE_CODE
                signupModel.MobileCode = mobileCode.text.toString().toInt()
                signupModel.Password = etPassword.text.toString()
                signupModel.isValid = 0
                callSignUpApi(signupModel)
            }

        } else {
            Toast.makeText(
                this@SignUpActivity,
                "connect to wifi or mobile network",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun callSignUpApi(model: SignUpRequestResponseModel) {
        viewModel.signUpApi(model).observe(this@SignUpActivity) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(this@SignUpActivity)
                    Log.d("response", "loading")
                }
                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data!!.code()==200){
                        Toast.makeText(this@SignUpActivity, apiResponse.data.body()!!.ResponseString, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "invalid user data", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("response", "stopLoader")
                }
                Status.ERROR -> {
                    AppUtils.stopLoader()
                    Log.d("response", "error loader")
                }
            }
        }
    }


    private fun isValidated(): Boolean {
        if (phoneNo.text.isEmpty()) {
            phoneNoError.visibility = (View.VISIBLE)
            phoneNoError.setText(R.string.field_required)
            return false
        } /*else if (mobileCode.text.toString().isEmpty()) {
            entityCodeError.visibility = (View.VISIBLE)
            entityCodeError.setText(R.string.field_required)
            return false
        }*/ else if (etPassword.text.toString().isEmpty()) {
            passwordError.visibility = (View.VISIBLE)
            passwordError.setText(R.string.field_required)
            return false
        } else if (etPassword.text.toString().length < 8) {
            passwordError.visibility = (View.VISIBLE)
            passwordError.setText(R.string.password_characters_length)
            return false
        } else if (etConfirmPassword.text.toString().isEmpty()) {
            confirmPasswordError.visibility = (View.VISIBLE)
            confirmPasswordError.setText(R.string.field_required)
            return false
        } else if (etConfirmPassword.text.toString().length < 8) {
            confirmPasswordError.visibility = (View.VISIBLE)
            passwordError.setText(R.string.password_characters_length)
            return false
        } else if (!(etConfirmPassword.text.toString().equals(etPassword.text.toString()))) {
            confirmPasswordError.visibility = (View.VISIBLE)
            confirmPasswordError.setText(R.string.field_required)
            return false
        } else {
            phoneNoError.visibility = (View.GONE)
            entityCodeError.visibility = (View.GONE)
            passwordError.visibility = (View.GONE)
            confirmPasswordError.visibility = (View.GONE)
            return true
        }

    }
}