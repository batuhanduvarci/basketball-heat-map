package com.example.basketballheatmap.common.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Batuhan Duvarci on 7.03.2021.
 */
data class UserModel(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String
)