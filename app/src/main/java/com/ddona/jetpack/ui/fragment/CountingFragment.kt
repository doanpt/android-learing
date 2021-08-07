package com.ddona.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ddona.jetpack.R
import com.ddona.jetpack.viewmodel.CountingViewModel

class CountingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val countingViewModel = ViewModelProvider(this).get(CountingViewModel::class.java)

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