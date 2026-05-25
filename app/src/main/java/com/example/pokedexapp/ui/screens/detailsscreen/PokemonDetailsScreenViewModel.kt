package com.example.pokedexapp.ui.screens.detailsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.PokemonRepository
import com.example.pokedexapp.ui.screens.listscreen.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailsScreenViewModel(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state

    fun loadPokemon(name: String) {
        viewModelScope.launch {
            try {
                _state.value = DetailUiState.Loading
                _state.value = DetailUiState.Success(pokemonRepository.getPokemonDetails(name))
            } catch (e: Exception) {
                _state.value = DetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

}