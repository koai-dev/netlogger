package com.koai.sample

import androidx.lifecycle.MutableLiveData
import com.koai.base.main.viewmodel.BaseViewModel
import com.koai.sample.model.LocationModel
import com.koai.sample.service.ApiService

class MainViewModel(private val apiService: ApiService?) : BaseViewModel() {
    private val _location = MutableLiveData<LocationModel?>()
    val location: MutableLiveData<LocationModel?> = _location

    fun getLocation(){
        launchCoroutine {
            val location = apiService?.getLocation()
            _location.postValue(location)
        }
    }

    fun clearLog() {
        launchCoroutine {
            _location.postValue(null)
        }
    }
}