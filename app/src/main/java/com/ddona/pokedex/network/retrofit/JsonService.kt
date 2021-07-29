package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.model.Comment
import com.ddona.pokedex.model.Post
import retrofit2.Call
import retrofit2.http.*


interface JsonService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts")
    suspend fun getPosts(
        @Query("userId") userId: Array<Int>,
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): List<Post>

    @GET("posts")
    suspend fun getPosts(@QueryMap parameters: Map<String, String>): List<Post>

    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") postId: Int): List<Comment>

    @GET
    suspend fun getComments(@Url url: String): List<Comment>

    //Param will be passed via body
    @POST("posts")
    suspend fun createPost(@Body post: Post): Post


    //Param will be passed via url, ex: http://xxx.com/post?userId=123&title="New%20Text&body=abc
    @FormUrlEncoded
    @POST("posts")
    suspend fun createPost(
        @Field("userId") userId: Int,
        @Field("title") title: String,
        @Field("body") text: String
    ): Post

    //Param will be passed via url, ex: http://xxx.com/post?userId=123&title="New%20Text&body=abc
    @FormUrlEncoded
    @POST("posts")
    suspend fun createPost(@FieldMap fields: Map<String, String>): Post
}