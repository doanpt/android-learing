package com.dvt.monthlywallpaper.screen.list

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dvt.monthlywallpaper.databinding.FragmentWallpapersBinding
import com.dvt.monthlywallpaper.di.component.DaggerColorsComponent
import com.dvt.monthlywallpaper.di.module.ColorsModule

class WallpapersFragment : Fragment() {
    private lateinit var binding: FragmentWallpapersBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(WallpapersViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!viewModel.hasBeenInitialized()) {
            var colorsComponent = DaggerColorsComponent.builder()
                    .colorsModule(ColorsModule(context!!.applicationContext))
                    .build()
            colorsComponent.inject(context!!.applicationContext)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWallpapersBinding.inflate(inflater, container, false)
        setupRecycler(binding.recyclerWallpaper)
        return binding.root
    }

    private fun setupRecycler(recyclerWallpaper: RecyclerView) {
        recyclerWallpaper.layoutManager = GridLayoutManager(context, if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2)
    }
}