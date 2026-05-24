package com.example.pokedexapp.data.mapper

import com.example.pokedexapp.data.remote.models.PokemonResult
import com.example.pokedexapp.domain.models.PokemonListEntry
import com.example.pokedexapp.utils.toSpriteUrl

fun PokemonResult.toPokemonList(): PokemonListEntry {
    val idString = this.url.trimEnd('/').substringAfterLast("/")
    val formattedNumber = "#${idString.padStart(3, '0')}"
    return PokemonListEntry(
        pokemonName = this.name,
        pokemonImageUrl = this.url.toSpriteUrl(),
        pokemonNumber = formattedNumber
    )
}

fun List<PokemonResult>.toDomainList() : List<PokemonListEntry> {
    return this.map {it.toPokemonList()}
}