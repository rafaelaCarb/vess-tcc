package com.example.appvess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class LayerData(var length: String = "", var score: String = "")

object EvaluationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        EvaluationScreenContent(
            onBackClick = { navigator.pop() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreenContent(onBackClick: () -> Unit) {
    var localPropriedade by remember { mutableStateOf("") }
    var avaliador by remember { mutableStateOf("") }
    var outrasInfo by remember { mutableStateOf("") }
    var selectedLayers by remember { mutableStateOf(1) }
    val layerDataList = remember { mutableStateListOf<LayerData>().apply { addAll(List(5) { LayerData() }) } }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.surface
        )
    )

    Scaffold(
        topBar = { EvaluationHeader(onBackClick = onBackClick) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 8.dp
            ) {
                Column {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Button(
                        onClick = { /* TODO: Lógica para avaliar */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .height(52.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            text = "CONCLUIR AVALIAÇÃO",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundBrush),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FormSection(title = "Informações Gerais") {
                    EvaluationTextField(label = "Local/propriedade (GPS)", value = localPropriedade, onValueChange = { localPropriedade = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    EvaluationTextField(label = "Avaliador", value = avaliador, onValueChange = { avaliador = it })
                }
            }

            item {
                FormSection(title = "Seleção de Camadas") {
                    val layerOptions = (1..5).map { "$it" }.toTypedArray()
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.requiredWidth(350.dp)) {
                        layerOptions.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = MaterialTheme.shapes.extraLarge,                                onClick = { selectedLayers = index + 1 },
                                selected = (index + 1) == selectedLayers
                            ) {
                                Text(label)
                            }
                        }
                    }
                }
            }

            items(selectedLayers) { layerIndex ->
                FormSection(title = "Detalhes da Camada ${layerIndex + 1}") {
                    val currentLayerData = layerDataList[layerIndex]
                    EvaluationTextField(
                        label = "Comprimento (cm)",
                        value = currentLayerData.length,
                        onValueChange = { layerDataList[layerIndex] = currentLayerData.copy(length = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EvaluationTextField(
                        label = "Nota VESS",
                        value = currentLayerData.score,
                        onValueChange = { layerDataList[layerIndex] = currentLayerData.copy(score = it) }
                    )
                }
            }

            item {
                FormSection("Recursos Adicionais") {
                    EvaluationTextField(label = "Outras informações importantes", value = outrasInfo, onValueChange = { outrasInfo = it }, minLines = 4)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { /* TODO: Câmera */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Câmera", modifier = Modifier.padding(end = 8.dp))
                        Text("ADICIONAR FOTO")
                    }
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
            IconButton(onClick = { /* TODO: Mostrar ajuda */ }) {
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
private fun EvaluationHeader(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Avaliação", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
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
fun EvaluationTextField(label: String, value: String, onValueChange: (String) -> Unit, minLines: Int = 1) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        minLines = minLines,
        shape = MaterialTheme.shapes.medium
    )
}