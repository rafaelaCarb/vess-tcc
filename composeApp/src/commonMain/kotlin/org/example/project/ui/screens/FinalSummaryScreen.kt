package com.example.appvess.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.appvess.viewmodel.EvaluationViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.example.project.data.ApiService
import org.example.project.data.model.Avaliacao
import kotlin.math.round
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

data class FinalSummaryScreen(private val viewModel: EvaluationViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val apiService = remember { ApiService() }
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val finalAverageScore = viewModel.getFinalAverageScore()
        val startTime = viewModel.startTime
        val endTime = Clock.System.now()
        val duration = (endTime - startTime).inWholeSeconds
        val scoreText = (round((finalAverageScore ?: 0f) * 100) / 100.0).toString()

        Scaffold(
            topBar = { EvaluationHeader(onBackClick = { navigator.pop() }, title = "Resumo Final da Avaliação") },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                Surface(shadowElevation = 8.dp) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val avaliacaoFinal = Avaliacao(
                                    id = 0,
                                    configuracaoId = viewModel.config.id,
                                    nomeAvaliacao = viewModel.nomeDaAvaliacaoGeral.value,
                                    avaliador = viewModel.config.nome ?: "Não definido",
                                    dataInicio = startTime.toString(),
                                    dataFim = endTime.toString(),
                                    dataCriacao = endTime.toString(),
                                    totalAmostras = viewModel.samples.size,
                                    escoreMedioGeral = finalAverageScore,
                                    resumoAvaliacao = "Total de ${viewModel.samples.size} amostras avaliadas.",
                                    descricaoManejoLocal = viewModel.descricaoManejo.value,
                                    informacoes = viewModel.outrasInformacoes.value,
                                    status = "FINALIZADA"
                                )

                                val savedId = apiService.saveAvaliacao(avaliacaoFinal)
                                if (savedId != null) {
                                    snackbarHostState.showSnackbar("Avaliação final salva com sucesso!")
                                    navigator.popUntilRoot()
                                } else {
                                    snackbarHostState.showSnackbar("Erro ao salvar avaliação final.")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(52.dp)
                    ) {
                        Text("SALVAR E FECHAR", fontWeight = FontWeight.Bold)
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Resultado Geral", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Text("Escore Médio Geral: $scoreText", style = MaterialTheme.typography.titleLarge)
                Text("Total de Amostras: ${viewModel.samples.size}", style = MaterialTheme.typography.bodyLarge)
                Text("Duração da Avaliação: $duration segundos", style = MaterialTheme.typography.bodyLarge)

                Spacer(Modifier.height(16.dp))

                Text("Descrição do Manejo:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(viewModel.descricaoManejo.value.ifBlank { "Nenhuma descrição fornecida." })

                Spacer(Modifier.height(16.dp))

                Text("Outras Informações:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(viewModel.outrasInformacoes.value.ifBlank { "Nenhuma informação adicional fornecida." })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EvaluationHeader(onBackClick: () -> Unit, title: String) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
