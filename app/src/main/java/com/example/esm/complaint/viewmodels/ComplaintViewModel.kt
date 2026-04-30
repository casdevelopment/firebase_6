package com.example.esm.complaint.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.complaint.models.ComplaintResponseList
import com.example.esm.complaint.models.ComplaintResponseModel
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ComplaintViewModel(private val repository: Repository): ViewModel() {
    fun stdComplaintList(model: IdentityModel) :LiveData<NetworkStates<Response<StudentResponseModel>>>{
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.stdComplaintList(model)))

            }catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }

    fun complaintRegistrationForRHSClient(model: RequestBody, file: MultipartBody.Part?) : LiveData<NetworkStates<Response<ComplaintModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.complaintRegistrationForRHSClient(model,file)))

            }catch (e: Exception){
                Log.d("complaintRegistrationForRHS", "Exception "+e.message)
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))

            }
        }

    }
    fun complaintRegistration(model: ComplaintModel) : LiveData<NetworkStates<Response<ComplaintModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.complaintRegistration(model)))

            }catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))

            }
        }

    }

    fun getComplaintResponseList(model: ComplaintResponseList) : LiveData<NetworkStates<Response<ComplaintResponseList>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.complaintResponseList(model)))

            }catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))

            }
        }

    }

    fun submitComplaintMessage(fields: HashMap<String, Any>) : LiveData<NetworkStates<Response<ComplaintResponseModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.submitComplaintMessage(fields)))

            } catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))

            }
        }

    }
}