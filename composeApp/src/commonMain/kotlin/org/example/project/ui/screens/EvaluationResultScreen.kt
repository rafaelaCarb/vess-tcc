package com.example.appvess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.appvess.viewmodel.EvaluationViewModel
import kotlin.math.round

data class EvaluationResultScreen(private val viewModel: EvaluationViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val lastSample = remember { viewModel.samples.lastOrNull() }

        if (lastSample == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erro: Nenhuma amostra encontrada.")
            }
            return
        }

        var descricaoManejo by viewModel.descricaoManejo
        var outrasInformacoes by viewModel.outrasInformacoes
        val scoreText = (round((lastSample.escoreMedio ?: 0f) * 100) / 100.0).toString()

        Scaffold(
            topBar = { EvaluationHeader(onBackClick = { navigator.pop() }, title = "Resultado da Amostra") },
            bottomBar = {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { navigator.push(FinalSummaryScreen(viewModel)) },
                            modifier = Modifier.weight(1f).height(52.dp)
                        ) {
                            Text("FINALIZAR")
                        }

                        Button(
                            onClick = { navigator.pop() },
                            modifier = Modifier.weight(1f).height(52.dp)
                        ) {
                            Text("PRÓXIMA AMOSTRA")
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Resultado para: ${lastSample.nomeAmostra}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Escore Médio da Amostra: $scoreText",
                    style = MaterialTheme.typography.titleLarge
                )

                FormSection(title = "Análise e Recomendações") {
                    EvaluationTextField(
                        label = "Descrição do Manejo para o Local",
                        value = descricaoManejo,
                        onValueChange = { descricaoManejo = it },
                        minLines = 4
                    )
                    Spacer(Modifier.height(16.dp))

                    Text("Resumo da Avaliação:", fontWeight = FontWeight.Bold)
                    Text("Baseado na amostra '${lastSample.nomeAmostra}', o escore médio foi de $scoreText. Recomenda-se [ação baseada no escore].")
                    Spacer(Modifier.height(16.dp))

                    EvaluationTextField(
                        label = "Outras Informações Importantes",
                        value = outrasInformacoes,
                        onValueChange = { outrasInformacoes = it },
                        minLines = 2
                    )
                }
            }
        }
    }
}

@Composable
private fun FormSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Info, contentDescription = "Ajuda sobre a seção", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp), content = content)
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

@Composable
fun EvaluationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        minLines = minLines,
        shape = MaterialTheme.shapes.medium
    )
}
