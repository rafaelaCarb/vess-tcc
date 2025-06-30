package org.example.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val username: String
)