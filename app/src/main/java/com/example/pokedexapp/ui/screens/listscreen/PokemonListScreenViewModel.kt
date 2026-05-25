package com.example.pokedexapp.ui.screens.listscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.pokedexapp.data.paging.PokemonPagingSource
import com.example.pokedexapp.domain.PokemonRepository
import com.example.pokedexapp.domain.models.PokemonListEntry
import com.example.pokedexapp.utils.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class PokemonListScreenViewModel(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val pokemonPagingFlow: Flow<PagingData<PokemonListEntry>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { PokemonPagingSource(pokemonRepository) }
    ).flow
        .cachedIn(viewModelScope)
        .combine(_searchQuery) { pagingData, query ->
            if(query.isBlank()) pagingData
            else pagingData.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = false)
            }
        }

    fun onSearchQuery(query: String) {
        _searchQuery.value = query
    }
}