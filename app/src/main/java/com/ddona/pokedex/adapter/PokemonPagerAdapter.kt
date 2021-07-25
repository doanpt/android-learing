package com.ddona.pokedex.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ddona.pokedex.fragments.PokemonJsonParserFragment
import com.ddona.pokedex.fragments.PokemonRetrofitFragment
import com.ddona.pokedex.fragments.StackOverFlowFragment

class PokemonPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PokemonJsonParserFragment()
            1 -> PokemonRetrofitFragment()
            2 -> StackOverFlowFragment()
            else -> throw NotImplementedError()
        }
    }
}