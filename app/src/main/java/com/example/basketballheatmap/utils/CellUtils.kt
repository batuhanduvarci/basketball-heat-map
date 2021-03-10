package com.example.basketballheatmap.utils

import android.graphics.Color
import com.example.basketballheatmap.common.models.HexagonModel
import com.example.basketballheatmap.common.models.ShotModel
import com.example.basketballheatmap.screens.MainActivity
import kotlin.math.abs

/**
 * Created by batuhan.duvarci on 3/10/21.
 */
object CellUtils {
    var imageWidth = 0

    var imageHeight = 0

    var hexagonModelList = arrayListOf<HexagonModel>()

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
            hexagonModelList.add(
                HexagonModel(
                    id = shot.id,
                    positionX = scaledPosX,
                    positionY = scaledPosY,
                    inOut = shot.inOut
                )
            )
        }
    }

    fun calculateHexagonDensity() {
        for (hexagon in hexagonModelList) {
            for (hexagonToCompare in hexagonModelList) {
                if (hexagon.positionX == hexagonToCompare.positionX && hexagon.positionY == hexagonToCompare.positionY) {
                    hexagon.density++
                }
            }
            //Bu fonksiyon birebir objeleri karşılaştırdığı için eksik sonuç verdi.Bu yüzden farklı yöntem kullandım
            //honeyComb.density = Collections.frequency(honeyCombModelList, honeyComb)
        }
        for (hexagon in hexagonModelList) {
            hexagon.color = calculateColor(hexagon)
        }
    }

    private fun calculateColor(hexagonModel: HexagonModel): Int {
        val repeatingList = arrayListOf<HexagonModel>()
        var inCount = 0.0

        for (hexagon in hexagonModelList) {
            if (hexagon.positionX == hexagonModel.positionX && hexagon.positionY == hexagonModel.positionY) {
                repeatingList.add(hexagon)
            }
        }

        for (hexagon in repeatingList) {
            if (hexagon.inOut) {
                inCount++
            }
        }

        val successRate = (inCount / hexagonModel.density).times(100)

        return when {
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