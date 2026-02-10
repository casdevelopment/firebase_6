package com.example.esm.feecard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.feecard.models.FeeCardMasterModel
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class FeeCardViewModel(private val repository: Repository): ViewModel() {
    fun studentFeeCard(model: IdentityModel):LiveData<NetworkStates<Response<FeeCardMasterModel>>> {
        return liveData(Dispatchers.IO){
                emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.studentFeeCard(model)))
            }
            catch (e:Exception){
                emit(NetworkStates.error( data = null, message = e.message?:"something went wrong"))
            }
        }

    }

}