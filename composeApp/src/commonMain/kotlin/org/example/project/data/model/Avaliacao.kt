package org.example.project.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Avaliacao(
    val id: Long,

    @SerialName("configuracao_id")
    val configuracaoId: Long,

    @SerialName("nome_avaliacao")
    val nomeAvaliacao: String?,

    @SerialName("data_inicio")
    val dataInicio: String?,

    @SerialName("data_fim")
    val dataFim: String?,

    @SerialName("data_criacao")
    val dataCriacao: String?,

    @SerialName("resumo_avaliacao")
    val resumoAvaliacao: String?,

    @SerialName("descricao_manejo_local")
    val descricaoManejoLocal: String?,

    @SerialName("total_amostras")
    val totalAmostras: Int?,

    @SerialName("escore_medio_geral")
    val escoreMedioGeral: Float?,

    val avaliador: String?,
    val informacoes: String?,
    val status: String?
)