package com.ddona.jetpack.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ddona.jetpack.databinding.FragmentDataBinding
import com.ddona.jetpack.ui.TestEventActivity
import com.ddona.jetpack.viewmodel.CountingViewModel
import com.ddona.jetpack.viewmodel.CountingViewModelFactory

class CountingOneFragment : Fragment() {
    private lateinit var binding: FragmentDataBinding
    private val countingViewModel: CountingViewModel by activityViewModels() {
        CountingViewModelFactory(application = requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = countingViewModel

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        countingViewModel.navigateToDetails.observe(this, {
            val intent = Intent(activity, TestEventActivity::class.java)
            startActivity(intent)
        })
    }
}