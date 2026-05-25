package com.example.pokedexapp.ui.screens.detailsscreen

import com.example.pokedexapp.domain.PokemonRepository
import com.example.pokedexapp.domain.models.PokemonDetail
import com.example.pokedexapp.domain.models.Stat
import com.example.pokedexapp.ui.screens.listscreen.DetailUiState
import com.example.pokedexapp.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PokemonRepository
    private lateinit var viewModel: PokemonDetailsScreenViewModel

    @Before
    fun setUp() {
        repository = mockk()
        viewModel = PokemonDetailsScreenViewModel(repository)
    }

    @Test
    fun `given viewModel is created, when state is observed, then initial state is Loading`() {
        assertEquals(DetailUiState.Loading, viewModel.state.value)
    }

    @Test
    fun `given repository returns success, when loadPokemon is called, then state transitions to Success`() = runTest {
        coEvery { repository.getPokemonDetails("bulbasaur") } returns buildPokemonDetail(name = "bulbasaur")

        viewModel.loadPokemon("bulbasaur")

        assertTrue(viewModel.state.value is DetailUiState.Success)
    }

    @Test
    fun `given repository returns success, when loadPokemon is called, then Success state contains the pokemon detail`() = runTest {
        val expected = buildPokemonDetail(id = 1, name = "bulbasaur")
        coEvery { repository.getPokemonDetails("bulbasaur") } returns expected

        viewModel.loadPokemon("bulbasaur")

        assertEquals(expected, (viewModel.state.value as DetailUiState.Success).pokemon)
    }

    @Test
    fun `given repository throws an exception, when loadPokemon is called, then state transitions to Error`() = runTest {
        coEvery { repository.getPokemonDetails(any()) } throws Exception("Network error")

        viewModel.loadPokemon("bulbasaur")

        assertTrue(viewModel.state.value is DetailUiState.Error)
    }

    @Test
    fun `given repository throws with a message, when loadPokemon is called, then Error state contains the error message`() = runTest {
        coEvery { repository.getPokemonDetails("missingno") } throws Exception("Pokemon not found")

        viewModel.loadPokemon("missingno")

        assertEquals("Pokemon not found", (viewModel.state.value as DetailUiState.Error).message)
    }

    @Test
    fun `given a previous non-loading state, when loadPokemon is called again, then state resets to Loading first`() = runTest {
        val slowRepository = mockk<PokemonRepository>()
        coEvery { slowRepository.getPokemonDetails(any()) } coAnswers {
            delay(1_000)
            buildPokemonDetail()
        }
        val vm = PokemonDetailsScreenViewModel(slowRepository)

        vm.loadPokemon("bulbasaur")
        advanceUntilIdle()
        assertTrue(vm.state.value is DetailUiState.Success)

        vm.loadPokemon("pikachu")
        assertEquals(DetailUiState.Loading, vm.state.value)

        advanceUntilIdle()
        assertTrue(vm.state.value is DetailUiState.Success)
    }

    @Test
    fun `given an empty name, when loadPokemon is called, then no crash occurs`() = runTest {
        coEvery { repository.getPokemonDetails("") } returns buildPokemonDetail()

        viewModel.loadPokemon("")

        assertTrue(viewModel.state.value is DetailUiState.Success)
    }

    @Test
    fun `given multiple rapid calls, when loadPokemon is called, then final state corresponds to the last call`() = runTest {
        val pikachu = buildPokemonDetail(id = 25, name = "pikachu")
        coEvery { repository.getPokemonDetails("bulbasaur") } returns buildPokemonDetail(id = 1, name = "bulbasaur")
        coEvery { repository.getPokemonDetails("charmander") } returns buildPokemonDetail(id = 4, name = "charmander")
        coEvery { repository.getPokemonDetails("pikachu") } returns pikachu

        viewModel.loadPokemon("bulbasaur")
        viewModel.loadPokemon("charmander")
        viewModel.loadPokemon("pikachu")

        assertEquals(pikachu, (viewModel.state.value as DetailUiState.Success).pokemon)
    }

    @Test
    fun `given repository throws a RuntimeException, when loadPokemon is called, then state transitions to Error`() = runTest {
        coEvery { repository.getPokemonDetails(any()) } throws RuntimeException("Unexpected runtime failure")

        viewModel.loadPokemon("bulbasaur")

        assertTrue(viewModel.state.value is DetailUiState.Error)
        assertEquals("Unexpected runtime failure", (viewModel.state.value as DetailUiState.Error).message)
    }

    @Test
    fun `given a name with special characters, when loadPokemon is called, then no crash occurs`() = runTest {
        coEvery { repository.getPokemonDetails("pokémon-#1!") } returns buildPokemonDetail()

        viewModel.loadPokemon("pokémon-#1!")

        assertTrue(viewModel.state.value is DetailUiState.Success)
    }

    private fun buildPokemonDetail(
        id: Int = 1,
        name: String = "bulbasaur",
        imageUrl: String = "https://example.com/img.png",
        types: List<String> = listOf("grass"),
        abilities: List<String> = listOf("overgrow"),
        height: Int = 7,
        weight: Int = 69,
        baseExperience: Int = 64,
        stats: List<Stat> = listOf(Stat(name = "hp", value = 45))
    ) = PokemonDetail(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = types,
        abilities = abilities,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        stats = stats
    )
}