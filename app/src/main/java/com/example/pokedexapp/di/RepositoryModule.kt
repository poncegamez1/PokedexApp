package com.example.pokedexapp.di

import com.example.pokedexapp.data.repository.PokemonImpl
import com.example.pokedexapp.domain.PokemonRepository
import org.koin.dsl.module

fun repositoryModule() = module {

    single<PokemonRepository> {
        PokemonImpl(get())
    }

}