package com.example.basketballheatmap.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.example.basketballheatmap.R
import com.example.basketballheatmap.common.models.HoneyCombModel
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
    private lateinit var binding : ActivityMainBinding
    private lateinit var mainActivityViewModel : MainActivityViewModel

    private lateinit var viewTreeObserver : ViewTreeObserver
    private lateinit var honeyCombModelList: ArrayList<HoneyCombModel>

    private var imageWidth = 0
    private var imageHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewTreeObserver = binding.courtImageView.viewTreeObserver
        honeyCombModelList = ArrayList()
        initUserInterface()
        getShotsData()
    }

    private fun initUserInterface() {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                binding.courtImageView.viewTreeObserver.removeOnPreDrawListener(this)
                imageWidth = binding.courtImageView.measuredWidth
                imageHeight = binding.courtImageView.measuredHeight
                calculateCoordinates(-2.43, 5.919)
                calculateCoordinates(-6.81, -7.528)
                calculateCoordinates(0.27, 0.796)
                calculateCoordinates(3.03, 5.919)
                calculateHoneyCombDensity()
                return true
            }
        })
    }

    private fun calculateCoordinates(positionX: Double, positionY: Double) {
        val measuredPosX = (positionX * imageWidth) / BASKETBALL_FIELD_WIDTH
        val scaledPosX = when(positionX < 0){
            true -> (imageWidth / 2) + measuredPosX / 2
            else -> (imageWidth / 2) + measuredPosX * 2
        }
        val measuredPosY = (positionY * imageHeight) / BASKETBALL_FIELD_HEIGHT
        val scaledPosY = when(positionY < 0){
            true -> abs(measuredPosY)
            else -> measuredPosY
        }
        honeyCombModelList.add(HoneyCombModel(scaledPosX, scaledPosY))
    }

    private fun calculateHoneyCombDensity(){
        /*val hexagon = AppCompatImageView(this)
        hexagon.setImageDrawable(getDrawable(R.drawable.ic_hexagon))
        hexagon.translationX = posX.toFloat()
        hexagon.translationY = posY.toFloat()
        basketballFieldContainer.addView(hexagon)*/
        for (obj in honeyCombModelList){
            val density = Collections.frequency(honeyCombModelList, obj)
            obj.density = density
        }
        drawHexagon()
    }

    private fun drawHexagon(){
        for (obj in honeyCombModelList){
            val hexagon = AppCompatImageView(this)
            hexagon.setImageDrawable(getDrawable(R.drawable.ic_hexagon))
            hexagon.translationX = obj.positionX.toFloat()
            hexagon.translationY = obj.positionY.toFloat()
            binding.basketballFieldContainer.addView(hexagon)
        }
    }

    private fun getShotsData(){
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.getShotsData()
        }
    }

    companion object {
        const val BASKETBALL_FIELD_WIDTH = 15
        const val BASKETBALL_FIELD_HEIGHT = 14
    }
}