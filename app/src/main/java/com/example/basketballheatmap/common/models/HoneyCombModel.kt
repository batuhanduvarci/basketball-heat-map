package com.example.basketballheatmap.common.models

import android.graphics.Color

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
data class HoneyCombModel(
    val positionX: Double,
    val positionY: Double,
    var density: Int = 0,
    val inOut: Boolean = false,
    var color: Int = Color.rgb(0, 0, 0)
)