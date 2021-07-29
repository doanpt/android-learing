package com.ddona.pokedex.model

import com.google.gson.annotations.SerializedName

data class Comment(
    private val postId: Int,
    private val id: Int,
    private val name: String,
    private val email: String,
    @SerializedName("body")
    private val text: String,
)
