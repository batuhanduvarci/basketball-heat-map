package com.example.basketballheatmap.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.example.basketballheatmap.R
import com.example.basketballheatmap.common.models.HoneyCombModel
import com.example.basketballheatmap.databinding.ActivityMainBinding
import com.example.basketballheatmap.presentation.extensions.hideLoading
import com.example.basketballheatmap.presentation.extensions.showLoading
import com.example.basketballheatmap.utils.CellUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var viewTreeObserver: ViewTreeObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewTreeObserver = binding.courtImageView.viewTreeObserver
        initUserInterface()
    }

    private fun initUserInterface() {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.courtImageView.viewTreeObserver.removeOnPreDrawListener(this)
                CellUtils.imageWidth = binding.courtImageView.measuredWidth
                CellUtils.imageHeight = binding.courtImageView.measuredHeight
                getShotsData()
                return true
            }
        })

        mainActivityViewModel.honeyCombList.observe(this, {
            drawHexagon(it)
        })
    }

    private fun drawHexagon(honeyCombModelList : ArrayList<HoneyCombModel>) {
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

    private fun getShotsData() {
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.getShotsData(1)
        }
    }

    companion object {
        const val BASKETBALL_FIELD_WIDTH = 15
        const val BASKETBALL_FIELD_HEIGHT = 14
    }
}