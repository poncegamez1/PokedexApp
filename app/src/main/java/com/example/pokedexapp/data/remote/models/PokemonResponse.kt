package com.example.pokedexapp.data.remote.models

data class PokemonResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int?,
    val types: List<TypeResponse>,
    val abilities: List<AbilityResponse>,
    val stats: List<StatXXResponse>,
    val sprites: SpritesResponse
)