package com.ddona.jetpack.ui.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddona.jetpack.adapter.PassengersAdapter
import com.ddona.jetpack.databinding.ActivityPagingBinding
import com.ddona.jetpack.network.PassengerClient
import com.ddona.jetpack.viewmodel.PassengersViewModel
import com.ddona.jetpack.viewmodel.PassengersViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class PagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagingBinding

    private val viewModel: PassengersViewModel by viewModels() {
        PassengersViewModelFactory(PassengerClient.invoke())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = PassengersAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)


        //use lifecycle-runtime-ktx in build.gradle
        lifecycleScope.launch {
            viewModel.passengers.collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }
}