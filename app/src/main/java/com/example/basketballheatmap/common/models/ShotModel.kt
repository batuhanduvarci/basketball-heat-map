package com.example.basketballheatmap.common.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
data class ShotModel(
    @SerializedName("point")
    val point: Long,
    @SerializedName("segment")
    val segment: Long,
    @SerializedName("_id")
    val id: String,
    @SerializedName("InOut")
    val inOut: Boolean,
    @SerializedName("ShotPosX")
    val shotPosX: Double,
    @SerializedName("ShotPosY")
    val shotPosY: Double
)