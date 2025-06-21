package org.example.project

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.example.project.ui.screens.HomeScreen
import org.example.project.ui.theme.AppVessTheme

@Composable
fun App() {
    AppVessTheme {
        Navigator(screen = HomeScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}