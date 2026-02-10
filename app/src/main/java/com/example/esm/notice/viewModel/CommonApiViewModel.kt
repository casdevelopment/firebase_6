package com.example.esm.notice.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.notice.models.NoticeModel
import com.example.esm.notice.models.NoticeResponseListModel
import com.example.esm.welcome.models.IdentityModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class CommonApiViewModel(private val repository: Repository) : ViewModel() {
    fun studentNoticeApi(model: IdentityModel): LiveData<NetworkStates<Response<List<NoticeModel>>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.studentNoticeApi(model)))

            }catch (e: Exception){
                emit(NetworkStates.error(data = null, message = e.message?:"something went wrong"))
            }
        }

    }


}