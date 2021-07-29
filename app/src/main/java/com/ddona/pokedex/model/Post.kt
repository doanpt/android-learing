package com.ddona.pokedex.model

import com.google.gson.annotations.SerializedName

data class Post(
    private val userId: String,
    private val id: Int = 0,
    private val title: String?,
    @SerializedName("body")
    private val text: String
)
