package org.example.project

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.example.project.ui.screens.HomeScreen

import org.example.project.ui.theme.AppVessTheme

@Composable
@Preview
fun App() {
    AppVessTheme {
        HomeScreen(
            onEvaluateClick = {
                // TODO: Lógica para quando o botão "AVALIAR" for clicado
                println("Botão AVALIAR clicado!")
            },
            onConfigClick = {
                // TODO: Lógica para quando o botão "Configurações" for clicado
                println("Botão Configurações clicado!")
            }
        )
    }
}