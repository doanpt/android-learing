package com.ddona.pokedex.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.ddona.pokedex.adapter.QuestionAdapter
import com.ddona.pokedex.databinding.FragmentDataBinding
import com.ddona.pokedex.network.xml.StackOverFlowParser
import com.ddona.pokedex.util.Const
import kotlinx.coroutines.*


class StackOverFlowFragment : Fragment() {
    private val job = Job()

    private val mainScope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var binding: FragmentDataBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater)
        binding.rvData.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        mainScope.launch {
            val questions = StackOverFlowParser.parserLink(Const.STACK_OVER_FLOW_URL)
            Log.d("doanpt","return ${questions.size}")
            withContext(Dispatchers.Main) {
                binding.rvData.adapter = QuestionAdapter(questions)
            }
        }
        return binding.root
    }
}