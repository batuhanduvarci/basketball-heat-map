package com.example.basketballheatmap.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basketballheatmap.common.messages.ShotsResponseModel
import com.example.basketballheatmap.service.ServiceInstance
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch

/**
 * Created by Batuhan Duvarci on 6.03.2021.
 */
@ActivityScoped
class MainActivityViewModel : ViewModel() {
    private var _response = MutableLiveData<ShotsResponseModel>()
    val response : LiveData<ShotsResponseModel> get() = _response

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    fun getShotsData(){
        viewModelScope.launch {
            _isLoading.value = true
            _response.value = ServiceInstance.serviceApiInstance.getShots()
        }
    }
}