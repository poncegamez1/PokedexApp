package com.example.pokedexapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    printLogger()
    appDeclaration()
    modules(
        networkModule(),
        viewModelModule(),
        repositoryModule(),
    )
}