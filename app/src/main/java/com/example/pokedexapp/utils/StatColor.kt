package com.example.pokedexapp.utils

import androidx.compose.ui.graphics.Color

fun statColor(value: Int): Color = when {
    value < 50  -> Color(0xFFE74C3C)
    value < 80  -> Color(0xFFF39C12)
    value < 100 -> Color(0xFF2ECC71)
    else        -> Color(0xFF3498DB)
}