package com.example.basketballheatmap.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basketballheatmap.common.messages.ShotsResponseModel
import com.example.basketballheatmap.common.models.HexagonModel
import com.example.basketballheatmap.common.models.ShotModel
import com.example.basketballheatmap.common.models.UserModel
import com.example.basketballheatmap.service.ServiceInstance
import com.example.basketballheatmap.utils.CellUtils
import com.example.basketballheatmap.utils.PlayerUtils
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

    private var _hexagonList = MutableLiveData<ArrayList<HexagonModel>>()
    val hexagonList : LiveData<ArrayList<HexagonModel>> get() = _hexagonList

    private var _userData = MutableLiveData<UserModel>()
    val userData : LiveData<UserModel> get() = _userData

    private var _successRate = MutableLiveData<Double>()
    val successRate : LiveData<Double> get() = _successRate

    fun getShotsData(player : Int){
        viewModelScope.launch {
            _isLoading.value = true
            shotsResponseModel = ServiceInstance.serviceApiInstance.getShots()
            _userData.value = shotsResponseModel.data[player - 1].user
            calculateCells(shotsResponseModel.data[player - 1].shots)
        }
    }

    private fun calculateCells(shotsData: List<ShotModel>) {
        viewModelScope.launch {
            for (shot in shotsData){
                CellUtils.calculateCoordinates(shot)
            }
            CellUtils.calculateHexagonDensity()
            _hexagonList.value = CellUtils.hexagonModelList
            CellUtils.hexagonModelList.clear()
            _successRate.value = PlayerUtils.calculateSuccessRate(shotsData)
            _isLoading.value = false
        }
    }
}