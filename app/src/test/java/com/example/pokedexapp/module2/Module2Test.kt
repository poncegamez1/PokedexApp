package com.example.pokedexapp.module2

import org.junit.Test

private fun removeChars(input: String, charsToRemove: Set<Char>): String {
    if (input.isEmpty() || charsToRemove.isEmpty()) return input
    val result = StringBuilder(input.length)

    for (index in 0..<input.length) {
        val char = input[index]
        if (char !in charsToRemove) {
            result.append(char)
        }
    }

    return result.toString()
}

class Module2Test {

    @Test
    fun `given input with chars to remove, when removeChars is called, then matching chars are removed`() {
        val response = removeChars("hello world", setOf('o', 'u'))
        assert(response == "hell wrld")
    }

    @Test
    fun `given an empty input string, when removeChars is called, then result is empty`() {
        val response = removeChars("", setOf('a'))
        assert(response == "")
    }

    @Test
    fun `given all chars should be removed, when removeChars is called, then result is empty`() {
        val response = removeChars("aabbcc", setOf('a', 'b', 'c'))
        assert(response == "")
    }

    @Test
    fun `given an empty set of chars to remove, when removeChars is called, then input is returned unchanged`() {
        val response = removeChars("abc", setOf())
        assert(response == "abc")
    }

    @Test
    fun `given input with accents, when removeChars is called, then accented chars are removed correctly`() {
        val response = removeChars("café with azúcar", setOf('é', 'á'))
        assert(response == "caf with azúcar")
    }

    @Test
    fun `given input with various spacing types, when removeChars is called, then spaces are removed correctly`() {
        val response = removeChars("line1\nline2 space", setOf('\n', ' '))
        assert(response == "line1line2space")
    }

    @Test
    fun `given input with symbols and math characters, when removeChars is called, then symbols are removed`() {
        val response = removeChars("user@domain.com ($100 + €50!)", setOf('@', '$', '€', '!', '(', ')'))
        assert(response == "userdomain.com 100 + 50")
    }

    @Test
    fun `given lookalike or unassigned special characters, when removeChars is called, then only exact matches are removed`() {
        val response = removeChars("áàâäa", setOf('a'))
        assert(response == "áàâä")
    }
}