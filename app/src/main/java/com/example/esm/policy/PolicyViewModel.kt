package com.example.esm.policy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class PolicyViewModel(private val repository: Repository): ViewModel() {


    fun getPolicyListBeacon(fields: HashMap<String, String>) :LiveData<NetworkStates<Response<PolicyData>>>{
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getPolicyListBeacon(fields)))

            }catch (e:Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))

            }
        }

    }


}