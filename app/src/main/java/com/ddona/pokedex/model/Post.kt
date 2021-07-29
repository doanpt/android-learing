package com.ddona.pokedex.model

import com.google.gson.annotations.SerializedName

data class Post(
    private val userId: String,
    private val id: Int,
    private val title: String,
    @SerializedName("body")
    private val text: String
)
