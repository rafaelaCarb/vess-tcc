package com.example.appvess.ui.screens

import org.example.project.data.SharedConfigurationManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.data.model.Configuracao

object ConfigScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ConfigScreenContent(
            onBackClick = { navigator.pop() },
            onNavigateToTerms = { navigator.push(TermsAndConditionsScreen) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreenContent(
    onBackClick: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val configState by SharedConfigurationManager.config
    val isLoading by SharedConfigurationManager.isLoading

    var nome by remember(configState) { mutableStateOf(configState?.nome ?: "") }
    var email by remember(configState) { mutableStateOf(configState?.email ?: "") }
    var pais by remember(configState) { mutableStateOf(configState?.pais ?: "Brasil") }
    var cidade by remember(configState) { mutableStateOf(configState?.cidadeEestado ?: "") }
    var idioma by remember(configState) { mutableStateOf(configState?.idioma ?: "Português (Brasil)") }

    var termsAccepted by remember { mutableStateOf(false) }

    val emailRegex = remember { Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") }
    val isFormValid by remember(nome, email, pais, cidade, termsAccepted) {
        derivedStateOf {
            nome.isNotBlank() && email.matches(emailRegex) && pais.isNotBlank() && cidade.isNotBlank() && termsAccepted
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Aceito os termos e condições de uso",
                            modifier = Modifier.clickable { onNavigateToTerms() },
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val configParaSalvar = Configuracao(
                                    id = configState?.id ?: 0,
                                    nome = nome,
                                    email = email,
                                    pais = pais,
                                    cidadeEestado = cidade,
                                    idioma = idioma,
                                    dataCriacao = configState?.dataCriacao,
                                    dataAtualizacao = null
                                )
                                val success = SharedConfigurationManager.saveConfig(configParaSalvar)
                                if (success) {
                                    snackbarHostState.showSnackbar("Configuração salva com sucesso!")
                                    delay(1000L)
                                    onBackClick()
                                } else {
                                    snackbarHostState.showSnackbar("Falha ao salvar a configuração.")
                                }
                            }
                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("SALVAR ALTERAÇÕES", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
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
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            ConfigTextField("Nome Completo", nome, { nome = it }, isError = nome.isBlank() && !isLoading)
                            ConfigTextField("E-mail", email, { email = it }, KeyboardType.Email, isError = !email.matches(emailRegex) && email.isNotEmpty())
                            ConfigTextField("País", pais, { pais = it }, isError = pais.isBlank() && !isLoading)
                            ConfigTextField("Cidade - Estado", cidade, { cidade = it }, isError = cidade.isBlank() && !isLoading)
                            ConfigTextField("Idioma", idioma, { idioma = it }, readOnly = true)
                        }
                    }
                }
                item {
                    OutlinedButton(
                        onClick = onNavigateToTerms,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Icon(Icons.Outlined.Article, contentDescription = "Termos", modifier = Modifier.padding(end = 8.dp))
                        Text("Ler os Termos e Condições")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = MaterialTheme.shapes.medium,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = if (label == "E-mail") "Formato de e-mail inválido" else "Campo obrigatório",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
