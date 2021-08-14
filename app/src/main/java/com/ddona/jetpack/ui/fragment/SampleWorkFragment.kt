package com.ddona.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.ddona.jetpack.databinding.FragmentWorkBinding
import com.ddona.jetpack.viewmodel.SampleWorkerViewModel
import com.ddona.jetpack.viewmodel.SampleWorkerViewModelFactory

class SampleWorkFragment : Fragment() {
    private lateinit var binding: FragmentWorkBinding
    private lateinit var viewModel: SampleWorkerViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            SampleWorkerViewModelFactory(requireActivity().application)
        ).get(SampleWorkerViewModel::class.java)
        viewModel.sampleWorkStatus.observe(viewLifecycleOwner, { workInfo ->
            val wasSuccess =
                if (!workInfo.isEmpty() && workInfo[0].state == WorkInfo.State.SUCCEEDED) {
                    workInfo[0].outputData.getBoolean("is_success", false)
                } else {
                    false
                }
            binding.tvStatus.text = "Status is $wasSuccess"
        })
        binding.btnStart.setOnClickListener {
            viewModel.downloadContent("https://doanpt.com")
        }
        return binding.root
    }
}