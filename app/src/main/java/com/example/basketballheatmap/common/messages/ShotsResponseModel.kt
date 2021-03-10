package com.example.basketballheatmap.common.messages

import com.example.basketballheatmap.common.models.DataModel
import com.google.gson.annotations.SerializedName

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
data class ShotsResponseModel(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<DataModel>
)