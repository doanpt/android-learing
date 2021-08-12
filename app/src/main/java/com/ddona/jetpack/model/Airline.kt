package com.ddona.jetpack.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Airline(
    val country: String,
    val established: String,
    @SerializedName("head_quaters")
    @Expose
    val headQuaters: String,
    val id: Int,
    val logo: String,
    val name: String,
    val slogan: String,
    val website: String
)