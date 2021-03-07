package com.example.basketballheatmap.common.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
data class DataModel(
    @SerializedName("user") val user: UserModel,
    @SerializedName("shots") val shots: List<ShotModel>
)