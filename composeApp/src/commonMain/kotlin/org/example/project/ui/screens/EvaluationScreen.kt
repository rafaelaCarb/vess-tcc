package com.example.appvess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import cafe.adriel.voyager.core.model.rememberScreenModel
import kotlinx.coroutines.launch
import com.example.appvess.viewmodel.EvaluationViewModel
import org.example.project.data.SharedConfigurationManager

object EvaluationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // Obtém o estado da configuração do gestor compartilhado
        val config by SharedConfigurationManager.config
        val isLoading by SharedConfigurationManager.isLoading

        when {
            // Mostra um indicador de carregamento enquanto a configuração é buscada
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            // Se a configuração não existir, mostra uma mensagem de erro e um botão para navegar
            config == null -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Configuração não encontrada.")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { navigator.push(ConfigScreen) }) {
                        Text("Ir para Configurações")
                    }
                }
            }
            // Se a configuração existir, cria o ViewModel com ela e exibe a tela
            else -> {
                val viewModel = rememberScreenModel { EvaluationViewModel(config!!) }
                EvaluationScreenContent(
                    viewModel = viewModel,
                    onNavigateToResult = {
                        navigator.push(EvaluationResultScreen(viewModel))
                    },
                    onBackClick = { navigator.pop() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreenContent(
    viewModel: EvaluationViewModel,
    onNavigateToResult: () -> Unit,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Binds UI state directly to the ViewModel
    var nomeAmostra by viewModel.nomeAmostraAtual
    var localizacao by viewModel.localizacaoAtual
    var selectedLayers by viewModel.numeroDeCamadasAtual
    val layerDataList = viewModel.camadasAtuais
    var outrasInfo by viewModel.outrasInformacoes

    Scaffold(
        topBar = { EvaluationHeader(onBackClick = onBackClick, title = "Adicionar Amostra") },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        // Validação simples
                        val hasErrors = localizacao.isBlank() ||
                                layerDataList.take(selectedLayers).any { it.length.isBlank() || it.score.isBlank() }

                        if (hasErrors) {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Preencha todos os campos obrigatórios.") }
                        } else {
                            viewModel.addCurrentSample()
                            onNavigateToResult()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp)
                ) {
                    Text("AVALIAR AMOSTRA", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FormSection(title = "Dados da Amostra") {
                    EvaluationTextField(label = "Nome da Amostra", value = nomeAmostra, onValueChange = { nomeAmostra = it })
                    Spacer(Modifier.height(16.dp))
                    EvaluationTextField(label = "Localização (GPS ou Manual)", value = localizacao, onValueChange = { localizacao = it })
                }
            }
            item {
                FormSection(title = "Seleção de Camadas") {
                    val layerOptions = (1..5).map { "$it" }
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        layerOptions.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = MaterialTheme.shapes.large,
                                onClick = { selectedLayers = index + 1 },
                                selected = (index + 1) == selectedLayers
                            ) { Text(label) }
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
                    Spacer(Modifier.height(16.dp))
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
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(onClick = { /* Câmera */ }, modifier = Modifier.fillMaxWidth()) {
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
    minLines: Int = 1,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        minLines = minLines,
        shape = MaterialTheme.shapes.medium,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } }
    )
}
