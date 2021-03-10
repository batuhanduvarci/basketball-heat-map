package com.example.basketballheatmap.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basketballheatmap.common.messages.ShotsResponseModel
import com.example.basketballheatmap.common.models.DataModel
import com.example.basketballheatmap.common.models.HoneyCombModel
import com.example.basketballheatmap.common.models.ShotModel
import com.example.basketballheatmap.service.ServiceInstance
import com.example.basketballheatmap.utils.CellUtils
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch

/**
 * Created by Batuhan Duvarci on 6.03.2021.
 */
@ActivityScoped
class MainActivityViewModel : ViewModel() {
    private lateinit var shotsResponseModel : ShotsResponseModel

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private var _honeyCombList = MutableLiveData<ArrayList<HoneyCombModel>>()
    val honeyCombList : LiveData<ArrayList<HoneyCombModel>> get() = _honeyCombList

    fun getShotsData(player : Int){
        viewModelScope.launch {
            _isLoading.value = true
            shotsResponseModel = ServiceInstance.serviceApiInstance.getShots()
            calculateCells(shotsResponseModel.data[player - 1].shots)
        }
    }

    private fun calculateCells(shotsData: List<ShotModel>) {
        viewModelScope.launch {
            for (shot in shotsData){
                CellUtils.calculateCoordinates(shot)
            }
            CellUtils.calculateHoneyCombDensity()
            _honeyCombList.value = CellUtils.honeyCombModelList
            _isLoading.value = false
        }
    }
}