package com.example.pokedexapp.domain

import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.PokemonListEntry

interface PokemonRepository {

    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonListEntry>

    suspend fun getPokemonDetails(name: String): PokemonDetail
}