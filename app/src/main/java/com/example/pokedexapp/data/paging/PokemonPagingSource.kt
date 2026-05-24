package com.example.pokedexapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokedexapp.domain.PokemonRepository

import com.example.pokedexapp.domain.models.PokemonListEntry

class PokemonPagingSource(
    private val repository: PokemonRepository
) : PagingSource<Int, PokemonListEntry>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonListEntry> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val pokemon = repository.getPokemonList(limit, offset)
            LoadResult.Page(
                data = pokemon,
                prevKey = if(offset == 0) null else offset - limit,
                nextKey = if(pokemon.isEmpty()) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonListEntry>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(state.config.pageSize)
        }
    }

}