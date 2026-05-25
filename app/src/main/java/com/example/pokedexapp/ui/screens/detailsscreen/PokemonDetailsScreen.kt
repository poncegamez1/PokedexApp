package com.example.pokedexapp.ui.screens.detailsscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.Stat
import com.example.pokedexapp.ui.components.ErrorContent
import com.example.pokedexapp.ui.components.LoadingContent
import com.example.pokedexapp.ui.screens.listscreen.DetailUiState
import com.example.pokedexapp.utils.statColor
import com.example.pokedexapp.utils.typeColor

@Composable
fun PokemonDetailsScreen(
    uiState: DetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    when (uiState) {
        is DetailUiState.Loading -> LoadingContent()
        is DetailUiState.Error -> ErrorContent(message = uiState.message, onRetry = onRetry)
        is DetailUiState.Success -> DetailContent(pokemon = uiState.pokemon, onBack = onBack)
    }
}

@Composable
private fun DetailContent(
    pokemon: PokemonDetail,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = { PokemonTopBar(name = pokemon.name, onBack = onBack) },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PokemonImageCard(imageUrl = pokemon.imageUrl, pokemonId = pokemon.id)
            TypesRow(types = pokemon.types)
            PhysicalInfoRow(
                height = pokemon.height,
                weight = pokemon.weight,
                baseExperience = pokemon.baseExperience
            )
            AbilitiesSection(abilities = pokemon.abilities)
            StatsSection(stats = pokemon.stats)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonTopBar(name: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = name.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.detail_back_cd)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        windowInsets = WindowInsets(0)
    )
}

@Composable
private fun PokemonImageCard(imageUrl: String, pokemonId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "#${pokemonId.toString().padStart(3, '0')}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun TypesRow(types: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        types.forEach { type ->
            TypeChip(type = type)
        }
    }
}

@Composable
private fun PhysicalInfoRow(height: Int, weight: Int, baseExperience: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(
            label = stringResource(R.string.detail_label_height),
            value = stringResource(R.string.detail_height_value, height / 10.0),
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            label = stringResource(R.string.detail_label_weight),
            value = stringResource(R.string.detail_weight_value, weight / 10.0),
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            label = stringResource(R.string.detail_label_base_exp),
            value = baseExperience.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AbilitiesSection(abilities: List<String>) {
    SectionCard(title = stringResource(R.string.detail_section_abilities)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            abilities.forEach { ability ->
                AbilityChip(ability = ability)
            }
        }
    }
}

@Composable
private fun StatsSection(stats: List<Stat>) {
    SectionCard(title = stringResource(R.string.detail_section_stats)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            stats.forEach { stat ->
                StatRow(stat = stat)
            }
        }
    }
}

@Composable
private fun TypeChip(type: String) {
    val color = typeColor(type)
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun AbilityChip(ability: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
    ) {
        Text(
            text = ability.replace("-", " ").replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 0.5.sp
            )
            content()
        }
    }
}

@Composable
private fun StatRow(stat: Stat) {
    val statName = stat.name
        .replace("-", " ")
        .split(" ")
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) stat.value / 255f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "stat_$statName"
    )

    LaunchedEffect(Unit) { animationPlayed = true }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = statName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(96.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stat.value.toString().padStart(3),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(28.dp)
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = statColor(stat.value),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}