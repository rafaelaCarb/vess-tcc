package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.example.project.data.SharedConfigurationManager
import org.example.project.ui.screens.HomeScreen
import org.example.project.ui.theme.AppVessTheme

@Composable
fun App() {
    LaunchedEffect(Unit) {
        SharedConfigurationManager.loadInitialConfig()
    }

    AppVessTheme {
        Navigator(screen = HomeScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}