package com.ddona.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ddona.jetpack.R
import com.ddona.jetpack.viewmodel.CountingViewModel

class RedditListFragment : Fragment() {
    private val countingViewModel: CountingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        val btnCount = view.findViewById<Button>(R.id.btnCount)
        val tvCounting = view.findViewById<TextView>(R.id.tvCount)
        countingViewModel.count.observe(viewLifecycleOwner, {
            tvCounting.text = it.toString()
        })

        btnCount.setOnClickListener {
            countingViewModel.increaseValue()
        }
        return view
    }
}