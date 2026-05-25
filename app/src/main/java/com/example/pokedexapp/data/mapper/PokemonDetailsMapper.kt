package com.example.pokedexapp.data.mapper

import com.example.pokedexapp.data.remote.models.PokemonResponse
import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.Stat

fun PokemonResponse.toDetailUi() = PokemonDetail(
    id = id,
    name = name,
    imageUrl = sprites.other?.official_artwork?.front_default
        ?: sprites.front_default
        ?: "",
    types = types.sortedBy { it.slot }.map { it.type.name },
    abilities = abilities.sortedBy { it.slot }.map { it.ability.name },
    height = height,
    weight = weight,
    baseExperience = base_experience ?: 0,
    stats = stats.map { Stat(name = it.stat.name, value = it.base_stat) }
)