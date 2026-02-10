package com.example.esm.alertssms.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import retrofit2.Response

class AlertSmsViewModel(private val repository: Repository): ViewModel() {
    fun studentSmsHistory(model: IdentityModel):LiveData<NetworkStates<Response<StudentResponseModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.studentSmsHistory(model)))

            }
            catch (e:Exception){
                emit(NetworkStates.error(data = null, message = e.message?: "something went wrong"))

            }

        }


    }
}