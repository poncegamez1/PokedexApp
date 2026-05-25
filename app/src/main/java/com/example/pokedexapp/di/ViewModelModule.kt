package com.example.pokedexapp.di

import com.example.pokedexapp.ui.screens.detailsscreen.PokemonDetailsScreenViewModel
import com.example.pokedexapp.ui.screens.listscreen.PokemonListScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() = module {

    viewModel { PokemonListScreenViewModel(get()) }
    viewModel { PokemonDetailsScreenViewModel(get()) }

}