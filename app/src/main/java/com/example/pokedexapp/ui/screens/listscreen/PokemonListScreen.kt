package com.example.pokedexapp.ui.screens.listscreen

import android.R.drawable.ic_menu_gallery
import android.R.drawable.ic_menu_report_image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.models.PokemonListEntry
import com.example.pokedexapp.ui.components.EmptyState
import com.example.pokedexapp.ui.components.ErrorContent
import com.example.pokedexapp.ui.components.LoadingContent
import com.example.pokedexapp.utils.Routes

@Composable
fun PokemonListScreen(
    navController: NavController,
    pokemonPagingItems: LazyPagingItems<PokemonListEntry>,
    searchQuery: String,
    onSearchBarQueryChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchBarQueryChange
        )
        when (val refreshState = pokemonPagingItems.loadState.refresh) {
            is LoadState.Loading -> LoadingContent()
            is LoadState.Error -> ErrorContent(
                message = refreshState.error.localizedMessage
                    ?: stringResource(R.string.list_error_unknown),
                onRetry = { pokemonPagingItems.retry() }
            )
            is LoadState.NotLoading -> {
                if (pokemonPagingItems.itemCount == 0) {
                    EmptyState()
                } else {
                    PokemonList(
                        pokemonPagingItems = pokemonPagingItems,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonList(
    pokemonPagingItems: LazyPagingItems<PokemonListEntry>,
    navController: NavController,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = pokemonPagingItems.itemCount,
            key = { index -> pokemonPagingItems[index]?.pokemonName ?: index }
        ) { index ->
            val entry = pokemonPagingItems[index]
            if (entry != null) {
                PokemonItem(entry = entry, navController = navController)
            }
        }
        when (val appendState = pokemonPagingItems.loadState.append) {
            is LoadState.Loading -> item { AppendLoadingItem() }
            is LoadState.Error -> item {
                ErrorContent(
                    message = appendState.error.localizedMessage
                        ?: stringResource(R.string.list_error_load_more),
                    onRetry = { pokemonPagingItems.retry() }
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun PokemonItem(
    entry: PokemonListEntry,
    navController: NavController,
) {
    Card(
        onClick = { navController.navigate(Routes.pokemonDetail(entry.pokemonName)) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp,
            hoveredElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PokemonThumbnail(imageUrl = entry.pokemonImageUrl, name = entry.pokemonName)
            PokemonInfo(
                name = entry.pokemonName,
                number = entry.pokemonNumber,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PokemonThumbnail(imageUrl: String, name: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = name,
        modifier = Modifier.size(64.dp),
        placeholder = painterResource(ic_menu_gallery),
        error = painterResource(ic_menu_report_image)
    )
}

@Composable
private fun PokemonInfo(name: String, number: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        PokemonNumberBadge(number = number)
        Text(
            text = name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PokemonNumberBadge(number: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
        tonalElevation = 0.dp
    ) {
        Text(
            text = number,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun AppendLoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.list_search_placeholder),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.list_search_clear_cd),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}