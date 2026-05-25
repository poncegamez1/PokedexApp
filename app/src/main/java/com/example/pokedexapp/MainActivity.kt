package com.example.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokedexapp.ui.screens.detailsscreen.PokemonDetailsScreen
import com.example.pokedexapp.ui.screens.detailsscreen.PokemonDetailsScreenViewModel
import com.example.pokedexapp.ui.screens.initialscreen.InitialScreen
import com.example.pokedexapp.ui.screens.listscreen.PokemonListScreen
import com.example.pokedexapp.ui.screens.listscreen.PokemonListScreenViewModel
import com.example.pokedexapp.ui.screens.passwordgeneratorscreen.PasswordGeneratorScreen
import com.example.pokedexapp.ui.screens.passwordgeneratorscreen.PasswordGeneratorViewModel
import com.example.pokedexapp.ui.theme.PokedexAppTheme
import com.example.pokedexapp.utils.Routes
import com.example.pokedexapp.utils.Routes.POKEMON_DETAIL
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexAppTheme {

                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.INITIAL,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(150))
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.End,
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.End,
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(150))
                        }
                    ) {
                        composable(Routes.INITIAL) {
                            InitialScreen(navController = navController)
                        }
                        composable(Routes.POKEMON_LIST) {
                            val viewModel: PokemonListScreenViewModel = koinViewModel()
                            val pokemonPagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()
                            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

                            PokemonListScreen(
                                navController = navController,
                                pokemonPagingItems = pokemonPagingItems,
                                searchQuery = searchQuery,
                                onSearchBarQueryChange = viewModel::onSearchQuery
                            )
                        }

                        composable(
                            route = POKEMON_DETAIL,
                            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: return@composable
                            val viewModel: PokemonDetailsScreenViewModel = koinViewModel()
                            val state = viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(Unit) {
                                viewModel.loadPokemon(pokemonName)
                            }

                            PokemonDetailsScreen(
                                uiState = state.value,
                                onBack = { navController.popBackStack() },
                                onRetry = { viewModel.loadPokemon(pokemonName) }
                            )
                        }
                        composable(Routes.PASSWORD_GENERATOR) {
                            val viewModel: PasswordGeneratorViewModel = koinViewModel()
                            val length by viewModel.length.collectAsState()
                            val useUppercase by viewModel.useUppercase.collectAsState()
                            val useNumbers by viewModel.useNumbers.collectAsState()
                            val useSymbols by viewModel.useSymbols.collectAsState()
                            val password by viewModel.password.collectAsState()
                            val copied by viewModel.copied.collectAsState()
                            val strength = remember(password) { viewModel.scorePassword(password) }

                            PasswordGeneratorScreen(
                                length = length,
                                useUppercase = useUppercase,
                                useNumbers = useNumbers,
                                useSymbols = useSymbols,
                                password = password,
                                copied = copied,
                                strength = strength,
                                onLengthChange = viewModel::onLengthChange,
                                onUppercaseToggle = viewModel::onUppercaseToggle,
                                onNumbersToggle = viewModel::onNumbersToggle,
                                onSymbolsToggle = viewModel::onSymbolsToggle,
                                onGeneratePassword = viewModel::generatePasswordFromState,
                                onTriggerCopyFeedback = viewModel::triggerCopyFeedback
                            )
                        }
                    }
                }
            }
        }
    }
}

