package com.example.esm.eventcalendar.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.eventcalendar.models.EventsResponseModel
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class EventsViewModel(private val repository: Repository):ViewModel() {
    fun calendarList(model: IdentityModel):LiveData<NetworkStates<Response<EventsResponseModel>>> {
        return liveData(Dispatchers.IO){
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.calendarList(model)))

            }catch (e:Exception){
                emit(NetworkStates.error(data = null, message = e.message?: "something went wrong"))


            }
        }
    }
}