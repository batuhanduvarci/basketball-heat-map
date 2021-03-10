package com.example.basketballheatmap.utils

import com.example.basketballheatmap.common.models.ShotModel

/**
 * Created by batuhan.duvarci on 3/10/21.
 */
object PlayerUtils {

    fun calculateSuccessRate(shotsData: List<ShotModel>): Double {
        var inCount = 0.0
        for (shot in shotsData) {
            if (shot.inOut) {
                inCount++
            }
        }
        return (inCount / shotsData.size).times(100)
    }
}