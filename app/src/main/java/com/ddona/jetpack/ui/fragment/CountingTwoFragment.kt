package com.ddona.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ddona.jetpack.databinding.FragmentDataBinding
import com.ddona.jetpack.viewmodel.CountingViewModel
import com.ddona.jetpack.viewmodel.CountingViewModelFactory

class CountingTwoFragment : Fragment() {
    private lateinit var binding: FragmentDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater, container, false)
        val countingViewModel = ViewModelProvider(
            requireActivity(),
            CountingViewModelFactory(requireActivity().application)
        ).get(CountingViewModel::class.java)


        countingViewModel.count.observe(viewLifecycleOwner, {
            binding.tvCount.text = it.toString()
        })

        binding.btnCount.setOnClickListener {
            countingViewModel.increaseValue()
        }
        return binding.root
    }
}