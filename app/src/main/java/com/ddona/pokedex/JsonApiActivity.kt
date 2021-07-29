package com.ddona.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        callSampleApi()
    }

    fun callSampleApi() {
        mainScope.launch {
            val posts = JsonApiClient.retrofitService.getPosts()
            Log.d("doanpt", "call sample result: ${posts.size}")
        }
    }
}