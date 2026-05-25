package com.example.pokedexapp.utils

import androidx.compose.ui.graphics.Color

fun typeColor(type: String): Color = when (type.lowercase()) {
    "fire"     -> Color(0xFFE25822)
    "water"    -> Color(0xFF4A90D9)
    "grass"    -> Color(0xFF5DAC4A)
    "electric" -> Color(0xFFF4D03F)
    "psychic"  -> Color(0xFFE91E8C)
    "ice"      -> Color(0xFF7ECFED)
    "dragon"   -> Color(0xFF5B6EE1)
    "dark"     -> Color(0xFF4A3728)
    "fairy"    -> Color(0xFFE091E0)
    "fighting" -> Color(0xFFB8312F)
    "flying"   -> Color(0xFF89AAE3)
    "poison"   -> Color(0xFF9B59B6)
    "ground"   -> Color(0xFFD4A84B)
    "rock"     -> Color(0xFF8B7355)
    "bug"      -> Color(0xFF8CB230)
    "ghost"    -> Color(0xFF6C5A8E)
    "steel"    -> Color(0xFF8E9DAD)
    "normal"   -> Color(0xFF9E9E9E)
    else       -> Color(0xFF9E9E9E)
}