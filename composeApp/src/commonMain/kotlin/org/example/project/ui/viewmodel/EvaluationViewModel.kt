package com.example.appvess.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.data.model.Configuracao

// Data class para guardar os dados de uma única amostra
data class SampleData(
    val nomeAmostra: String,
    val localizacao: String,
    val camadas: List<LayerData>,
    val escoreMedio: Float?
)

// Data class para os detalhes de uma camada
data class LayerData(val length: String, val score: String)

// O ViewModel agora recebe a configuração no seu construtor.
class EvaluationViewModel(
    val config: Configuracao // Torna a configuração acessível publicamente
) : ScreenModel {
    // --- Dados da Avaliação Geral ---
    val startTime: Instant = Clock.System.now()
    var descricaoManejo = mutableStateOf("")
    var outrasInformacoes = mutableStateOf("")

    // **AJUSTE:** Gera um nome padrão para a avaliação, que pode ser editado.
    var nomeDaAvaliacaoGeral = mutableStateOf(generateDefaultEvaluationName())

    // --- Lista de Amostras Adicionadas ---
    val samples = mutableStateListOf<SampleData>()

    // --- Dados da Amostra Atualmente em Edição ---
    var nomeAmostraAtual = mutableStateOf("Amostra 1")
    var localizacaoAtual = mutableStateOf("")
    var numeroDeCamadasAtual = mutableStateOf(1)
    val camadasAtuais = mutableStateListOf<LayerData>().apply {
        addAll(List(5) { LayerData("", "") })
    }

    fun addCurrentSample() {
        val activeLayers = camadasAtuais.take(numeroDeCamadasAtual.value)
        val averageScore = activeLayers
            .mapNotNull { it.score.toFloatOrNull() }
            .takeIf { it.isNotEmpty() }
            ?.average()?.toFloat()

        samples.add(
            SampleData(
                nomeAmostra = nomeAmostraAtual.value,
                localizacao = localizacaoAtual.value,
                camadas = activeLayers,
                escoreMedio = averageScore
            )
        )
        resetCurrentSampleFields()
    }

    private fun resetCurrentSampleFields() {
        nomeAmostraAtual.value = "Amostra ${samples.size + 1}"
        localizacaoAtual.value = ""
        numeroDeCamadasAtual.value = 1
        camadasAtuais.clear()
        camadasAtuais.addAll(List(5) { LayerData("", "") })
    }

    fun getFinalAverageScore(): Float? {
        return samples
            .mapNotNull { it.escoreMedio }
            .takeIf { it.isNotEmpty() }
            ?.average()?.toFloat()
    }

    private fun generateDefaultEvaluationName(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date = "${now.dayOfMonth.toString().padStart(2, '0')}/${now.monthNumber.toString().padStart(2, '0')}/${now.year}"
        val time = "${now.hour.toString().padStart(2, '0')}h${now.minute.toString().padStart(2, '0')}"
        return "Avaliação $date - $time"
    }
}
