package com.ddona.jetpack.ui.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ddona.jetpack.R
import com.ddona.jetpack.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding

    private val args: WelcomeFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.tvUserName.text = args.username
        binding.tvPassword.text = args.password
        binding.buttonOk.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

}