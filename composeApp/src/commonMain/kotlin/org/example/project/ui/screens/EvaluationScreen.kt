package com.example.appvess.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    onBackClick: () -> Unit
) {
    var selectedLayers by remember { mutableStateOf(1) }
    var localPropriedade by remember { mutableStateOf("") }
    var avaliador by remember { mutableStateOf("") }
    var comprimentoCamada1 by remember { mutableStateOf("") }
    var notaCamada1 by remember { mutableStateOf("") }
    var comprimentoCamada2 by remember { mutableStateOf("") }
    var comprimentoCamada3 by remember { mutableStateOf("") }
    var notaCamada3 by remember { mutableStateOf("") }
    var outrasInfo by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        ),
        startY = 0f,
        endY = 800f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        EvaluationHeader(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Amostra Nº 01",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { /* TODO: Info */ },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Text(
                            text = "?",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Text(
                text = "Quantas camadas de solo deseja avaliar?",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(5) { index ->
                    val layerNumber = index + 1
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedLayers == layerNumber) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
                            )
                            .clickable { selectedLayers = layerNumber },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = layerNumber.toString(),
                            color = if (selectedLayers == layerNumber) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            EvaluationTextField(
                label = "Local/propriedade (GPS):",
                value = localPropriedade,
                onValueChange = { localPropriedade = it }
            )

            EvaluationTextField(
                label = "Avaliador:",
                value = avaliador,
                onValueChange = { avaliador = it }
            )

            EvaluationTextField(
                label = "Comprimento camada 1:",
                value = comprimentoCamada1,
                onValueChange = { comprimentoCamada1 = it }
            )

            EvaluationTextField(
                label = "Nota camada 1:",
                value = notaCamada1,
                onValueChange = { notaCamada1 = it }
            )

            EvaluationTextField(
                label = "Comprimento camada 2:",
                value = comprimentoCamada2,
                onValueChange = { comprimentoCamada2 = it }
            )

            if (selectedLayers >= 3) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Avaliações Camada 3",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { /* TODO: Info */ },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Text(
                                text = "?",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                EvaluationTextField(
                    label = "Comprimento camada 3:",
                    value = comprimentoCamada3,
                    onValueChange = { comprimentoCamada3 = it }
                )

                EvaluationTextField(
                    label = "Nota camada 3:",
                    value = notaCamada3,
                    onValueChange = { notaCamada3 = it }
                )
            }


            Button(
                onClick = { /* TODO: Camera */ },
                modifier = Modifier
                    .size(64.dp) // Slightly larger button
                    .align(Alignment.CenterHorizontally), // Center the button
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt, // Changed to a more relevant camera icon
                    contentDescription = "Câmera",
                    modifier = Modifier.size(32.dp)
                )
            }

            EvaluationTextField(
                label = "Outras informações importantes:",
                value = outrasInfo,
                onValueChange = { outrasInfo = it },
                minLines = 4
            )

            Text(
                text = "Sugestões que contribuam para a construção de um histórico de avaliação da propriedade ou do grau de sucesso de uma cultura ou de lavoura específica da cultura avaliada (...)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Button(
                onClick = { /* TODO: Avaliar */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Consistent height with ModernEvaluateButton
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium, // Use MaterialTheme shape
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "AVALIAR",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun EvaluationHeader(onBackClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp), // Rounded bottom corners
        shadowElevation = 12.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(gradientBrush)
                .padding(vertical = 16.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp) // Slightly larger icon
                    )
                }
                Text(
                    text = "Avaliações",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(48.dp)) // To balance the back button
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.medium
        )
    }
}