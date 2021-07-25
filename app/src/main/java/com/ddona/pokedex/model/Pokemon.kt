package com.ddona.pokedex.model

class Pokemon(var id: Int = 0, var name: String, var url: String) {
    fun getImageUrl(): String {
        val index = url.split("/")
        return ("https://pokeres.bastionbot.org/images/pokemon/"
                + index[index.size - 2] + ".png")
    }
}
