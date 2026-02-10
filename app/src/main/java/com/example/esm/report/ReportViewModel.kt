package com.example.esm.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.esm.network.NetworkStates
import com.example.esm.network.koin_module.Repository
import com.example.esm.welcome.models.IdentityModel
import com.example.esm.welcome.models.StudentResponseModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class ReportViewModel(private val repository: Repository): ViewModel() {

    fun getEvaluationType(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<ReportDataModel>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getEvaluationType(fields)))

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
    fun getMiddleEvaluationType(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<ReportDataModel>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getMiddleEvaluationType(fields)))

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
    fun getOALevelEvaluationType(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<ReportDataModel>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getOALevelEvaluationType(fields)))

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


    fun getEvaluation(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<ReportDataModel>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getEvaluation(fields)))

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
    fun getMiddleEvaluation(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<ReportDataModel>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getMiddleEvaluation(fields)))

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
    fun getOALevelEvaluation(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<ReportDataModel>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getOALevelEvaluation(fields)))

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


    fun getMYEReport(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getMYEReport(fields)))

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

    fun getEOYReport(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getEOYReport(fields)))

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
    fun getMiddleReport(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getMiddleReport(fields)))

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
    fun getOALevelReport(fields: HashMap<String, Any>): LiveData<NetworkStates<Response<String>>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkStates.loading(null))
            try {
                emit(NetworkStates.success(data = repository.getOALevelReport(fields)))

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