package com.ddona.pokedex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ddona.pokedex.model.Post
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
//        getComments()
//        getCommentOfPostId()
//        getPosts()
//        getPostsUsingQuery()
//        createPost()
//        updatePost()
        deletePost()
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

    private fun createPost() {
        val post = Post("25", 25, "New Title", "New text")
        val fields: MutableMap<String, String> = HashMap()
        fields["userId"] = "25"
        fields["body"] = "New text"
        fields["title"] = "New Title"
        mainScope.launch {
            val postResult = JsonApiClient.retrofitService
                .createPost(post)
            Log.d("doanpt", "create post return: $postResult")

            val postResult2 = JsonApiClient.retrofitService
                .createPost(fields)
            Log.d("doanpt", "create post return: $postResult2")

            val postResult3 = JsonApiClient.retrofitService
                .createPost(25, "New Title", "New text")
            Log.d("doanpt", "create post return: $postResult3")
        }
    }

    private fun updatePost() {
        val post = Post("12", 5, title = null, text = "Abc")
        mainScope.launch {
            val putResult = JsonApiClient.retrofitService
                .putPost(5, post)
            Log.d("doanpt", "update post with put annotation return: $putResult")
            val pathResults = JsonApiClient.retrofitService
                .patchPost(5, post)
            Log.d("doanpt", "update post with patch annotation return: $pathResults")
        }
    }

    private fun deletePost() {
        mainScope.launch {
            val result = JsonApiClient.retrofitService
                .deletePost(5)
            Log.d("doanpt", "Deleted post: ${result.isSuccessful} - ${result.code()}")
        }
    }
}