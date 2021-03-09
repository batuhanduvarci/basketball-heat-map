package com.example.basketballheatmap.screens

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.example.basketballheatmap.R
import com.example.basketballheatmap.common.models.HoneyCombModel
import com.example.basketballheatmap.common.models.ShotModel
import com.example.basketballheatmap.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var viewTreeObserver: ViewTreeObserver
    private lateinit var honeyCombModelList: ArrayList<HoneyCombModel>
    private lateinit var brokenHoneyCombModelList: ArrayList<HoneyCombModel>

    private var imageWidth = 0
    private var imageHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewTreeObserver = binding.courtImageView.viewTreeObserver
        honeyCombModelList = ArrayList()
        brokenHoneyCombModelList = ArrayList()
        initUserInterface()
        //getShotsData()
    }

    private fun initUserInterface() {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.courtImageView.viewTreeObserver.removeOnPreDrawListener(this)
                imageWidth = binding.courtImageView.measuredWidth
                imageHeight = binding.courtImageView.measuredHeight
                getShotsData()
                return true
            }
        })

        mainActivityViewModel.response.observe(this, { response ->
            val shotsData = response.data[0].shots
            for (shotData in shotsData) {
                calculateCoordinates(shotData)
            }
            calculateHoneyCombDensity()
        })
    }

    private fun calculateCoordinates(shot: ShotModel) {
        val measuredPosX = (shot.shotPosX * imageWidth) / BASKETBALL_FIELD_WIDTH
        val scaledPosX = when (shot.shotPosX < 0) {
            true -> (imageWidth / 2) + measuredPosX
            else -> (imageWidth / 2) + measuredPosX
        }
        val measuredPosY = (shot.shotPosY * imageHeight) / BASKETBALL_FIELD_HEIGHT
        val scaledPosY = when (shot.shotPosY < 0) {
            true -> abs(measuredPosY)
            else -> measuredPosY
        }
        if (scaledPosX in 0.0..imageWidth.toDouble()) {
            honeyCombModelList.add(HoneyCombModel(scaledPosX, scaledPosY, inOut = shot.inOut))
        }
    }

    private fun calculateHoneyCombDensity() {
        for (honeyComb in honeyCombModelList) {
            for (honeyCombToCompare in honeyCombModelList){
                if (honeyComb.positionX == honeyCombToCompare.positionX && honeyComb.positionY == honeyCombToCompare.positionY){
                    honeyComb.density++
                }
            }
            //honeyComb.density = Collections.frequency(honeyCombModelList, honeyComb)
        }
        for (honeyComb in honeyCombModelList){
            honeyComb.color = calculateColor(honeyComb)
        }
        drawHexagon()
    }

    private fun drawHexagon() {
        for (obj in honeyCombModelList) {
            val hexagon = AppCompatImageView(this)
            when (obj.density) {
                1 -> hexagon.setImageDrawable(getDrawable(R.drawable.ic_hexagon_1))
                2 -> hexagon.setImageDrawable(getDrawable(R.drawable.ic_hexagon_2))
                3 -> hexagon.setImageDrawable(getDrawable(R.drawable.ic_hexagon_3))
                else -> hexagon.setImageDrawable(getDrawable(R.drawable.ic_hexagon_4))
            }
            hexagon.setColorFilter(obj.color)
            hexagon.translationX = obj.positionX.toFloat()
            hexagon.translationY = obj.positionY.toFloat()
            binding.basketballFieldContainer.addView(hexagon)
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

        return when((inCount / honeyCombModel.density).times(100)){
            in 0.0..12.5 -> Color.rgb(178, 24, 43)
            in 12.5..25.0 -> Color.rgb(244, 109, 67)
            in 25.0..37.5 -> Color.rgb(253, 174, 97)
            in 37.5..50.0 -> Color.rgb(254, 224, 139)
            in 50.0..62.5 -> Color.rgb(230, 245, 152)
            in 62.5..75.0 -> Color.rgb(171, 221, 164)
            in 75.0..87.5 -> Color.rgb(102, 194, 165)
            in 87.5..100.0 -> Color.rgb(50, 136, 189)
            else -> Color.rgb(0, 0, 0)
        }
    }

    private fun getShotsData() {
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.getShotsData()
        }
    }

    companion object {
        const val BASKETBALL_FIELD_WIDTH = 15
        const val BASKETBALL_FIELD_HEIGHT = 14
    }
}