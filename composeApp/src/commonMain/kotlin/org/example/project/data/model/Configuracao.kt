// No seu projeto do aplicativo (cliente)
// Arquivo: org/example/project/data/model/Configuracao.kt

package org.example.project.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Configuracao(
    val id: Long,
    val nome: String?,
    val email: String?,
    val pais: String?,
    val idioma: String?,
    @SerialName("cidade_e_estado")
    val cidadeEestado: String?,
    @SerialName("data_criacao")
    val dataCriacao: String?,
    @SerialName("data_atualizacao")
    val dataAtualizacao: String?
)