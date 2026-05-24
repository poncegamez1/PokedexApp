package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.mapper.toDetailUi
import com.example.pokedexapp.data.mapper.toDomainList
import com.example.pokedexapp.data.remote.PokemonApi
import com.example.pokedexapp.domain.PokemonRepository
import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.PokemonListEntry

class PokemonImpl(
    private val pokemonApi: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonListEntry> {
        return pokemonApi.getPokemonList(limit, offset).results.toDomainList()
    }

    override suspend fun getPokemonDetails(name: String): PokemonDetail {
        return pokemonApi.getPokemonDetails(name).toDetailUi()
    }
}