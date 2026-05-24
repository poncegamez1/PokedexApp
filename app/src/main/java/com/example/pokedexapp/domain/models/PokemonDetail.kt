package com.example.pokedexapp.domain.models

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val stats: List<Stat>
)
