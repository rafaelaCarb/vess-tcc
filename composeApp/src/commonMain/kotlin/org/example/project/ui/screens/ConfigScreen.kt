package com.example.appvess.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import com.example.appvess.ui.screens.TermsAndConditionsScreen



    object ConfigScreen : Screen {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            ConfigScreenContent(
                onBackClick = { navigator.pop() }
            )
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreenContent(onBackClick: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("Brasil") }
    var endereco by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("São José dos Campos - SP") }
    var idioma by remember { mutableStateOf("Português (Brasil)") }

    val navigator = LocalNavigator.currentOrThrow
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.surface
        )
    )

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
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = { /* TODO: Salvar Alterações */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(52.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("SALVAR ALTERAÇÕES", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        ConfigTextField(label = "Nome Completo", value = nome, onValueChange = { nome = it })
                        ConfigTextField(label = "E-mail", value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)
                        ConfigTextField(label = "País", value = pais, onValueChange = { pais = it })
                        ConfigTextField(label = "Endereço (Opcional)", value = endereco, onValueChange = { endereco = it })
                        ConfigTextField(label = "Cidade - Estado (Opcional)", value = cidade, onValueChange = { cidade = it })
                        ConfigTextField(label = "Idioma", value = idioma, onValueChange = { idioma = it }, readOnly = true)
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = { navigator.push(TermsAndConditionsScreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(Icons.Outlined.Article, contentDescription = "Termos", modifier = Modifier.padding(end = 8.dp))
                    Text("Termos e condições de uso")
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
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = MaterialTheme.shapes.medium
    )
}