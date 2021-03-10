package com.example.basketballheatmap.utils

import android.graphics.Color
import com.example.basketballheatmap.common.models.HoneyCombModel
import com.example.basketballheatmap.common.models.ShotModel
import com.example.basketballheatmap.screens.MainActivity
import kotlin.math.abs

/**
 * Created by batuhan.duvarci on 3/10/21.
 */
object CellUtils {
    var imageWidth = 0

    var imageHeight = 0

    var honeyCombModelList = arrayListOf<HoneyCombModel>()

    fun calculateCoordinates(shot: ShotModel) {
        val measuredPosX = (shot.shotPosX * imageWidth) / MainActivity.BASKETBALL_FIELD_WIDTH
        val scaledPosX = when (shot.shotPosX < 0) {
            true -> (imageWidth / 2) + measuredPosX
            else -> (imageWidth / 2) + measuredPosX
        }
        val measuredPosY = (shot.shotPosY * imageHeight) / MainActivity.BASKETBALL_FIELD_HEIGHT
        val scaledPosY = when (shot.shotPosY < 0) {
            true -> abs(measuredPosY)
            else -> measuredPosY
        }
        if (scaledPosX in 0.0..imageWidth.toDouble()) {
            honeyCombModelList.add(HoneyCombModel(scaledPosX, scaledPosY, inOut = shot.inOut))
        }
    }

    fun calculateHoneyCombDensity() {
        for (honeyComb in honeyCombModelList) {
            for (honeyCombToCompare in honeyCombModelList){
                if (honeyComb.positionX == honeyCombToCompare.positionX && honeyComb.positionY == honeyCombToCompare.positionY){
                    honeyComb.density++
                }
            }
            //Bu fonksiyon birebir objeleri karşılaştırdığı için eksik sonuç verdi.Bu yüzden farklı yöntem kullandım
            //honeyComb.density = Collections.frequency(honeyCombModelList, honeyComb)
        }
        for (honeyComb in honeyCombModelList){
            honeyComb.color = calculateColor(honeyComb)
        }
    }

    private fun calculateColor(honeyCombModel: HoneyCombModel): Int {
        val repeatingList = arrayListOf<HoneyCombModel>()
        var inCount = 0.0

        for (honeyComb in honeyCombModelList){
            if (honeyComb.positionX == honeyCombModel.positionX && honeyComb.positionY == honeyCombModel.positionY){
                repeatingList.add(honeyComb)
            }
        }

        for (honeyComb in repeatingList){
            if (honeyComb.inOut){
                inCount++
            }
        }

        val successRate = (inCount / honeyCombModel.density).times(100)

        return when{
            successRate >= 0.0 && successRate < 12.5 -> Color.rgb(178, 24, 43)
            successRate >= 12.5 && successRate < 25.0 -> Color.rgb(244, 109, 67)
            successRate >= 25.0 && successRate < 37.5 -> Color.rgb(253, 174, 97)
            successRate >= 37.5 && successRate < 50.0 -> Color.rgb(254, 224, 139)
            successRate >= 50.0 && successRate < 62.5 -> Color.rgb(230, 245, 152)
            successRate >= 62.5 && successRate < 75.0 -> Color.rgb(171, 221, 164)
            successRate >= 75.0 && successRate < 87.5 -> Color.rgb(102, 194, 165)
            successRate in 87.5..100.0 -> Color.rgb(50, 136, 189)
            else -> Color.rgb(0, 0, 0)
        }
    }


}