package com.example.pokedexapp.utils

object Routes {
    const val POKEMON_LIST = "pokemon_list_screen"
    const val POKEMON_DETAIL = "pokemon_detail_screen/{pokemonName}"

    fun pokemonDetail(name: String) = "pokemon_detail_screen/$name"
}