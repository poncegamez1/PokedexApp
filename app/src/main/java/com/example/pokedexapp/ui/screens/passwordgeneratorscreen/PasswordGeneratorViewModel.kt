package com.example.pokedexapp.ui.screens.passwordgeneratorscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.utils.Constants.DIGITS
import com.example.pokedexapp.utils.Constants.LOWERCASE
import com.example.pokedexapp.utils.Constants.SYMBOLS
import com.example.pokedexapp.utils.Constants.UPPERCASE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.SecureRandom

class PasswordGeneratorViewModel : ViewModel() {

    private val RNG = SecureRandom()

    private val _length = MutableStateFlow(16)
    val length: StateFlow<Int> = _length.asStateFlow()

    private val _useUppercase = MutableStateFlow(true)
    val useUppercase: StateFlow<Boolean> = _useUppercase.asStateFlow()

    private val _useNumbers = MutableStateFlow(true)
    val useNumbers: StateFlow<Boolean> = _useNumbers.asStateFlow()

    private val _useSymbols = MutableStateFlow(true)
    val useSymbols: StateFlow<Boolean> = _useSymbols.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _copied = MutableStateFlow(false)
    val copied: StateFlow<Boolean> = _copied.asStateFlow()

    fun onLengthChange(newLength: Int) {
        _length.value = newLength
    }

    fun onUppercaseToggle(value: Boolean) {
        _useUppercase.value = value
    }

    fun onNumbersToggle(value: Boolean) {
        _useNumbers.value = value
    }

    fun onSymbolsToggle(value: Boolean) {
        _useSymbols.value = value
    }

    fun generatePasswordFromState() {
        val generated = generatePassword(
            length = _length.value,
            useUppercase = _useUppercase.value,
            useNumbers = _useNumbers.value,
            useSymbols = _useSymbols.value
        )
        _password.value = generated
        _copied.value = false
    }

    fun generatePassword(
        length: Int,
        useUppercase: Boolean = true,
        useNumbers: Boolean = true,
        useSymbols: Boolean = true
    ): String {
        require(length >= 4) { "Password length must be at least 4 (got $length)." }

        val alphabet = buildString {
            append(LOWERCASE)
            if (useUppercase) append(UPPERCASE)
            if (useNumbers) append(DIGITS)
            if (useSymbols) append(SYMBOLS)
        }

        if (alphabet.isEmpty()) return ""

        val guaranteed = mutableListOf<Char>()
        if (useUppercase) guaranteed += UPPERCASE[RNG.nextInt(UPPERCASE.length)]
        if (useNumbers) guaranteed += DIGITS[RNG.nextInt(DIGITS.length)]
        if (useSymbols) guaranteed += SYMBOLS[RNG.nextInt(SYMBOLS.length)]

        val remainingLength = maxOf(0, length - guaranteed.size)
        val all = (guaranteed + List(remainingLength) {
            alphabet[RNG.nextInt(alphabet.length)]
        }).toMutableList()

        for (i in all.indices.reversed()) {
            val j = RNG.nextInt(i + 1)
            val tmp = all[i]; all[i] = all[j]; all[j] = tmp
        }

        return all.joinToString("")
    }

    fun triggerCopyFeedback() {
        viewModelScope.launch {
            _copied.value = true
            delay(1500)
            _copied.value = false
        }
    }

    fun scorePassword(pw: String): PasswordStrength {
        if (pw.isEmpty()) return PasswordStrength.WEAK
        var score = 0
        if (pw.length >= 12) score++
        if (pw.length >= 16) score++
        if (pw.any { it.isUpperCase() }) score++
        if (pw.any { it.isDigit() }) score++
        if (pw.any { it in SYMBOLS }) score++
        return when {
            score <= 2 -> PasswordStrength.WEAK
            score <= 3 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }
}