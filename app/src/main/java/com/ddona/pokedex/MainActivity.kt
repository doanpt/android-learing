package com.ddona.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ddona.pokedex.network.json.PokemonParser
import com.ddona.pokedex.util.Const
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val job = Job()

    private val mainScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainScope.launch {
            val pokemons =
                PokemonParser.getListPokemon(Const.BASE_URL.plus(Const.POKEMON_LIST_PATH))
            Log.d("doanpt", "get ${pokemons.size}")
        }
    }
}