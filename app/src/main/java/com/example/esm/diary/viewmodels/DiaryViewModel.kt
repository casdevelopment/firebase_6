package com.example.esm.diary.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.diary.models.DiaryResponseModel
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class DiaryViewModel(private val repository: Repository) : ViewModel() {

    fun getStudentDiary(model: IdentityModel): LiveData<NetworkStates<Response<DiaryResponseModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getStudentDiary(model)))

            }catch (e:Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }
}