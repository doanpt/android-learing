package com.ddona.pokedex.model

class PokemonResponse(
    var count: Int,
    var next: String,
    var previous: String,
    var results: List<Pokemon>
)