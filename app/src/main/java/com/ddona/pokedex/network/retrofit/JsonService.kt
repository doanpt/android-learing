package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.model.Post
import retrofit2.Call

import retrofit2.http.GET




interface JsonService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}