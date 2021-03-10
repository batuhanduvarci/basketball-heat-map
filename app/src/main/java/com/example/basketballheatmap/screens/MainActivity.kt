package com.example.basketballheatmap.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.example.basketballheatmap.R
import com.example.basketballheatmap.common.models.HexagonModel
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

    private var selectedPlayer = 1

    private var hexagonViewList = arrayListOf<AppCompatImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewTreeObserver = binding.courtImageView.viewTreeObserver
        initUserInterface()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.switchPlayers -> {
                when(selectedPlayer){
                    1 -> {
                        selectedPlayer++
                    }
                    else -> {
                        selectedPlayer--
                    }
                }
                deleteHexagon()
                mainActivityViewModel.getShotsData(selectedPlayer)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

        mainActivityViewModel.hexagonList.observe(this, {
            drawHexagon(it)
        })

        mainActivityViewModel.userData.observe(this, {
            binding.nameTextView.text = String.format(getString(R.string.player_name_text), it.name, it.surname)
        })

        mainActivityViewModel.successRate.observe(this, {
            binding.successRateTextView.text = String.format(getString(R.string.success_rate_text), it)
        })

        mainActivityViewModel.isLoading.observe(this, {
            when(it){
                true -> showLoading(supportFragmentManager)
                false -> hideLoading(supportFragmentManager)
            }
        })
    }

    private fun drawHexagon(hexagonModelList : ArrayList<HexagonModel>) {
        for (obj in hexagonModelList) {
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
            hexagonViewList.add(hexagon)
        }
    }

    private fun deleteHexagon(){
        for (obj in hexagonViewList){
            binding.basketballFieldContainer.removeView(obj)
        }
    }

    private fun getShotsData() {
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.getShotsData(selectedPlayer)
        }
    }

    companion object {
        const val BASKETBALL_FIELD_WIDTH = 15
        const val BASKETBALL_FIELD_HEIGHT = 14
    }
}