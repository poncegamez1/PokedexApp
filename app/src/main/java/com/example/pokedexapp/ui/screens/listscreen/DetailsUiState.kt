package com.example.pokedexapp.ui.screens.listscreen

import com.example.pokedexapp.domain.models.PokemonDetail

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val pokemon: PokemonDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}