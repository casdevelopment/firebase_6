package com.example.esm.studenttimetable.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.studenttimetable.models.TimeTableModel
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class TimeTableViewModel( private val repository: Repository) :ViewModel() {
    fun stdTimeTable(model: IdentityModel) :LiveData<NetworkStates<Response<StudentResponseModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.stdTimeTable(model)))
            }
            catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))

            }
        }

    }
}