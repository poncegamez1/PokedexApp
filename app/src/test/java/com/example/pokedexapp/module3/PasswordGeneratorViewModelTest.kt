package com.example.pokedexapp.module3

import com.example.pokedexapp.ui.screens.passwordgeneratorscreen.PasswordGeneratorViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class PasswordGeneratorViewModelTest {

    private lateinit var viewModel: PasswordGeneratorViewModel

    @Before
    fun setup() {
        viewModel = PasswordGeneratorViewModel()
    }

    @Test
    fun `given specific length, when generatePassword is called, then result length equals the parameter`() {
        val targetLength = 14

        val result = viewModel.generatePassword(length = targetLength)

        assertEquals(targetLength, result.length)
    }

    @Test
    fun `given useNumbers is true, when generatePassword is called, then result contains at least one digit`() {
        val result = viewModel.generatePassword(
            length = 10,
            useUppercase = false,
            useNumbers = true,
            useSymbols = false
        )

        val containsDigit = result.any { it.isDigit() }
        assertTrue(containsDigit)
    }

    @Test
    fun `given consecutive calls, when generatePassword is called, then they do not return the same value`() {
        val firstPassword = viewModel.generatePassword(length = 16)
        val secondPassword = viewModel.generatePassword(length = 16)

        assertNotEquals(firstPassword, secondPassword)
    }

    @Test
    fun `given length is 2, when generatePassword is called, then throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            viewModel.generatePassword(length = 2)
        }
    }
}