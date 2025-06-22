package com.example.appvess.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object TermsAndConditionsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var accepted by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Termos e Condições", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                )
            },
            bottomBar = {
                Surface(shadowElevation = 8.dp) {
                    Button(
                        enabled = accepted,
                        onClick = { navigator.pop() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .height(52.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("FECHAR", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "O presente termo e condições de uso visa regular a utilização dos USUÁRIOS ao Aplicativo VESS. Ao acessar o Aplicativo VESS, o USUÁRIO expressamente aceita e concorda com as disposições destes Termos e Condições de Uso.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                item {
                    SectionTitle("DO OBJETIVO")
                    Text(
                        text = "Este aplicativo é uma ferramenta gratuita de uso, desenvolvido para fornecer aos agricultores, pesquisadores e profissionais da área agrícola uma avaliação prática, acessível e de baixo custo para avaliar a qualidade da estrutura do solo. O aplicativo permite que os usuários concluam uma autoavaliação sobre suas práticas agrícolas a partir da qualidade estrutural do solo obtida, sugerindo melhorias nas práticas de manejo e contribuindo para melhorar a sustentabilidade em suas ações de manejo do solo.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    SectionTitle("COMUNICAÇÕES")
                    Text(
                        text = "O aplicativo VESS disponibiliza o endereço de e-mail rachelguimaraes@utfpr.edu.br como o Canal de Atendimento para receber as comunicações do USUÁRIO.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    SectionTitle("COMPARTILHAMENTO DE DADOS COM OS DESENVOLVEDORES")
                    Text(
                        text = "Os desenvolvedores do aplicativo têm como princípio da atuação o respeito ao USUÁRIO, agindo sempre em conformidade com as disposições do Marco Civil da Internet (Lei Federal n. 12965/14) e com a Lei Geral de Proteção de Dados Pessoais (Lei 13.709/18). Ao compartilhar os resultados das avaliações com os desenvolvedores você possibilita que mais pesquisas e melhorias no aplicativo sejam realizadas. Dados pessoais como nome, E-mail e coordenadas de localização não serão divulgados). O aplicativo pode ser acessado por qualquer dispositivo móvel conectado ou não à Internet, independentemente de localização geográfica. Em vista das diferenças que podem existir entre as legislações locais e nacionais, ao acessar o aplicativo, o USUÁRIO concorda que a legislação aplicável para fins destes Termos e Condições de Uso será aquela vigente na República Federativa do Brasil.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { accepted = !accepted }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = accepted,
                            onCheckedChange = { accepted = it }
                        )
                        Text(
                            text = "Li e concordo com os termos e condições de uso do aplicativo.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(4.dp))
}