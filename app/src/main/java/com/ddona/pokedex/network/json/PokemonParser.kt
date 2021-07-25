package com.ddona.pokedex.network.json

import androidx.core.util.rangeTo
import com.ddona.pokedex.model.Pokemon
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

object PokemonParser {

    fun getListPokemon(link: String): List<Pokemon> {
        val url = URL(link)
        val http = url.openConnection()
        val br = BufferedReader(InputStreamReader(http.getInputStream()))
        val data = StringBuilder()
        var line = br.readLine()
        while (line != null) {
            data.append(line)
            line = br.readLine()
        }
        val rootObj = JSONObject(data.toString())
        val pokeList: JSONArray = rootObj.getJSONArray("results")
        val results = mutableListOf<Pokemon>()
        for (i in 0 until pokeList.length()) {
            val pokemonObj: JSONObject = pokeList[i] as JSONObject
            results.add(Pokemon(i, pokemonObj.getString("name"), pokemonObj.getString("url")))
        }
        return results.toList()
    }
}