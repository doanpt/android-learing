package com.ddona.jetpack.ui.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddona.jetpack.adapter.PassengersAdapter
import com.ddona.jetpack.adapter.PassengersLoadStateAdapter
import com.ddona.jetpack.databinding.ActivityPagingBinding
import com.ddona.jetpack.network.PassengerClient
import com.ddona.jetpack.viewmodel.PassengersViewModel
import com.ddona.jetpack.viewmodel.PassengersViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

//https://developer.android.com/topic/libraries/architecture/paging/v3-overview
//https://www.simplifiedcoding.net/android-jetpack-paging-3/
@AndroidEntryPoint
class PagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagingBinding

    //We are using Hilt, so we don't need to provide ViewModelProviderFactory
    private val viewModel: PassengersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = PassengersAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PassengersLoadStateAdapter { adapter.retry() },
            footer = PassengersLoadStateAdapter { adapter.retry() }
        )
        binding.recyclerView.setHasFixedSize(true)


        //use lifecycle-runtime-ktx in build.gradle
        lifecycleScope.launch {
            viewModel.passengers.collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }
}