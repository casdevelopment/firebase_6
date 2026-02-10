package com.example.esm.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.signup.model.SignUpRequestResponseModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class SignUpViewModel(private val repository: Repository):ViewModel() {
    fun signUpApi(model: SignUpRequestResponseModel) :LiveData<NetworkStates<Response<SignUpRequestResponseModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.signUpApi(model)))

            }catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
                e.message?.let { Log.v("response", it) }

            }
        }

    }
}