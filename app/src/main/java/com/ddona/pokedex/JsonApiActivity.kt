package com.ddona.pokedex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ddona.pokedex.network.retrofit.JsonApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class JsonApiActivity : AppCompatActivity() {
    private val job = Job()

    private val mainScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_api)
//        callSampleApi()
        getComments()
        getCommentOfPostId()
        getPosts()
        getPostsUsingQuery()
    }

    private fun callSampleApi() {
        mainScope.launch {
            val posts = JsonApiClient.retrofitService.getPosts()
            Log.d("doanpt", "call sample result: ${posts.size}")
        }
    }

    private fun getComments() {
        mainScope.launch {
            val comments = JsonApiClient.retrofitService
                .getComments("https://jsonplaceholder.typicode.com/posts/3/comments")
            Log.d("doanpt", "get comments using url annotation return: ${comments.size}")
        }
    }

    private fun getCommentOfPostId() {
        mainScope.launch {
            val comments = JsonApiClient.retrofitService
                .getComments(1)
            Log.d("doanpt", "get comments using path annotation return: ${comments.size}")
        }
    }

    private fun getPosts() {
        mainScope.launch {
            val parameters: MutableMap<String, String> = HashMap()
            parameters["userId"] = "1"
            parameters["_sort"] = "id"
            parameters["_order"] = "desc"
            val posts = JsonApiClient.retrofitService
                .getPosts(parameters)
            Log.d("doanpt", "get posts using query map annotation return: ${posts.size}")
        }
    }

    private fun getPostsUsingQuery() {
        mainScope.launch {
            val posts = JsonApiClient.retrofitService
                .getPosts(arrayOf(1), "id", "desc")
            Log.d("doanpt", "get posts using query annotation return: ${posts.size}")
        }
    }
}