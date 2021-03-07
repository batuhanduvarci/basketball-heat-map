package com.example.basketballheatmap.service

import com.example.basketballheatmap.common.constants.ServiceConstants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
class ServiceInstance {
    companion object {
        private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY)
        private val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()

        val serviceApiInstance: ServiceApi by lazy {
            retrofit.create(ServiceApi::class.java)
        }
    }
}