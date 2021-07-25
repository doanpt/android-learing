package com.ddona.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddona.pokedex.adapter.PokemonPagerAdapter
import com.ddona.pokedex.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tabTitles = listOf("Pokemon", "Favorite", "StackOF")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.vpPokemon.adapter = PokemonPagerAdapter(this)
        TabLayoutMediator(binding.tapPokemon, binding.vpPokemon) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}