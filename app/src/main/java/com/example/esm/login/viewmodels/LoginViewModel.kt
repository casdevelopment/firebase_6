package com.example.esm.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.login.models.LoginRequestResponseModel
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun loginApi(model: LoginRequestResponseModel): LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.loginApi(model)))
            } catch (e: Exception) {
                emit(
                    NetworkStates.error(
                        data = null,
                        message = e.message ?: "something went wrong"
                    )
                )
            }

        }


    }


}