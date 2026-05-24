package com.example.pokedexapp.data.remote.models

data class VersionGroupDetail(
    val level_learned_at: Int,
    val move_learn_method: MoveLearnMethod,
    val order: Any,
    val version_group: VersionGroup
)