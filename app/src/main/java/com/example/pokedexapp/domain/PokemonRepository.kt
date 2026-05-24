package com.example.pokedexapp.domain

import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.PokemonList

interface PokemonRepository {

    suspend fun getPokemonList(limit: Int, offset: Int): PokemonList

    suspend fun getPokemonDetails(name: String): PokemonDetail
}