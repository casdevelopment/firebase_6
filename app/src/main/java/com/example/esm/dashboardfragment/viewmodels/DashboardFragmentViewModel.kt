package com.example.esm.dashboardfragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.dashboardfragment.models.DashboardFragmentModel
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import retrofit2.Response

class DashboardFragmentViewModel(private val repository: Repository): ViewModel() {
    fun downloadVoucherApi(model: DashboardFragmentModel) :LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.downloadVoucherApi(model)))

            } catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }

    fun downloadVoucherApiAlRazi(model: DashboardFragmentModel) :LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.downloadVoucherApiAlRazi(model)))

            } catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }

    fun updatePassword(fields: HashMap<String, String>) :LiveData<NetworkStates<Response<String>>> {
        Log.v("showRunning", "updatePassword  " )
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.updatePassword(fields)))

            } catch (e: Exception){
                Log.v("showRunning", "updatePassword Exception  "+e.message )
                Log.v("showRunning", "updatePassword Exception  "+e.localizedMessage )
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }
    fun saveNotificationApi(fields: HashMap<String, String>) :LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.saveNotificationApi(fields)))

            } catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }

    fun applyLeave(fields: HashMap<String, String>) :LiveData<NetworkStates<Response<JSONObject>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.applyLeave(fields)))

            } catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }
}