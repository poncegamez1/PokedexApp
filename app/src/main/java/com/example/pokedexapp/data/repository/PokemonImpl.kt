package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.remote.PokemonApi
import com.example.pokedexapp.domain.PokemonRepository
import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.PokemonList

class PokemonImpl(
    private val pokemonApi: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): PokemonList {
        TODO("Not yet implemented")
    }

    override suspend fun getPokemonDetails(name: String): PokemonDetail {
        TODO("Not yet implemented")
    }
}