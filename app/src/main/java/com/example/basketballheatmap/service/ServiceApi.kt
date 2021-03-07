package com.example.basketballheatmap.service

import com.example.basketballheatmap.common.messages.ShotsResponseModel
import retrofit2.http.GET

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
interface ServiceApi {
    @GET("/shots")
    suspend fun getShots(): ShotsResponseModel
}