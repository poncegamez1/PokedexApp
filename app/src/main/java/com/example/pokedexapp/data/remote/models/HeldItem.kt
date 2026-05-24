package com.example.pokedexapp.data.remote.models

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)