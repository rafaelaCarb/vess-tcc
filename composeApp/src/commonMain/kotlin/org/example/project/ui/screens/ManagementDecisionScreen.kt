package com.example.appvess.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object ManagementDecisionScreen : Screen {
    @Composable
    override fun Content() {
        ManagementDecisionContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementDecisionContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Decisão de Manejo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextSection(
                title = "Sobre o método VESS",
                content = """
O método VESS fornece uma avaliação da qualidade estrutural atual do solo e permite decisões de manejo do solo que visam melhorar ou manter a qualidade do solo.

Para aliar a VESS à decisão de manejo do solo, são recomendadas múltiplas amostras (3 a 5), avaliadas preferencialmente por um único avaliador.
                """.trimIndent()
            )

            TextSection(
                title = "Interpretação dos Escores VESS",
                content = """
Escores entre 1 a 2,9:
Amostras (0-25 cm) com escores Qe-VESS entre 1–2,9 indicam boa qualidade estrutural e não requerem mudanças no manejo.

Escores entre 3 a 3,9:
Indicam qualidade razoável com possibilidade de melhoria. Mudanças de manejo devem incluir rotação de culturas com raízes profundas e matéria seca, além de reduzir compactação com menor tráfego de máquinas ou superlotação animal.

Escores entre 4 a 5:
Indicam danos às funções do solo, como porosidade e resistência. Quando próximo à superfície, há limitação agronômica e necessidade de intervenção direta.
                """.trimIndent()
            )

            TextSection(
                title = "Recomendações de Manejo",
                content = """
A decisão de manejo deve considerar indicadores visuais e medidas como acúmulo de água, estresse nas culturas, profundidade de raízes, relevo, densidade do solo, porosidade, resistência à penetração e dados de rendimento.

Mudanças podem incluir revolvimento mecânico ou uso de culturas com raízes abundantes entre safras para melhoria a curto prazo.

Fonte: Ball et al. (2017)
                """.trimIndent()
            )
        }
    }
}

@Composable
fun TextSection(title: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp
        )
    }
}
