package org.example.project.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.appvess.ui.screens.ComplementaryInfoScreen
import com.example.appvess.ui.screens.ConfigScreen
import com.example.appvess.ui.screens.EvaluationScreen
import com.example.appvess.ui.screens.ManagementDecisionScreen
import kotlinx.coroutines.launch
import org.example.project.data.SharedConfigurationManager

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        HomeScreenContent(
            onEvaluateClick = { navigator.push(EvaluationScreen) },
            onConfigClick = { navigator.push(ConfigScreen) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    onEvaluateClick: () -> Unit,
    onConfigClick: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val config by SharedConfigurationManager.config
    val isLoadingConfig by SharedConfigurationManager.isLoading

    val processMenuItems = listOf(
        MenuItem("Equipamentos", Icons.Default.Build) { navigator.push(EquipmentScreen) },
        MenuItem("Onde amostrar", Icons.Default.LocationOn) {},
        MenuItem("Quando amostrar", Icons.Default.Schedule) {},
        MenuItem("Extração", Icons.Default.ContentCut) {},
        MenuItem("Fragmentação", Icons.Default.Grain) {},
        MenuItem("Escores VESS", Icons.Default.Rule) {}
    )

    val extrasMenuItems = listOf(
        MenuItem("Decisão de manejo", Icons.Default.Spa) { navigator.push(ManagementDecisionScreen) },
        MenuItem("Minhas avaliações", Icons.Default.History) {},
        MenuItem("O que é o VESS", Icons.Outlined.HelpOutline) {},
        MenuItem("Info. Complementar", Icons.Outlined.Info) { navigator.push(ComplementaryInfoScreen) },
        MenuItem("Configurações", Icons.Default.Settings, onClick = onConfigClick)
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("VESS", fontWeight = FontWeight.Bold) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                HeaderContent()
                Spacer(modifier = Modifier.height(24.dp))
                EvaluateButton(
                    onClick = {
                        coroutineScope.launch {
                            if (isLoadingConfig) {
                                snackbarHostState.showSnackbar("Aguarde, carregando configuração...")
                            } else if (config == null) {
                                snackbarHostState.showSnackbar("Configure o aplicativo antes de iniciar uma avaliação.")
                                onConfigClick()
                            } else {
                                onEvaluateClick()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                SectionTitle("Processo de Avaliação")
                MenuCarousel(items = processMenuItems)
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                SectionTitle("Mais Opções")
                MenuCarousel(items = extrasMenuItems)
            }
        }
    }
}

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun HeaderContent() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Bem-vindo",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Visual Evaluation of Soil Structure",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluateButton(onClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    )
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.background(gradientBrush).padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Iniciar Nova Avaliação",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Comece a analisar a qualidade do solo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
    )
}

@Composable
fun MenuCarousel(items: List<MenuItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            MenuCard(item = item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(item: MenuItem) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "card_scale"
    )
    Surface(
        onClick = item.onClick,
        modifier = Modifier.scale(scale).width(130.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).height(130.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
object EquipmentScreen : Screen {
    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Tela de Equipamentos")
        }
    }
}
