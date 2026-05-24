package com.example.pokedexapp.utils

fun String.extractPokemonId(): Int? =
    trimEnd('/').substringAfterLast('/').toIntOrNull()

fun String.toSpriteUrl(): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${extractPokemonId()}.png"