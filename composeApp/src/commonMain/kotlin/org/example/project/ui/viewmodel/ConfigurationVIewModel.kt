package com.example.appvess.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.example.project.data.ApiService
import org.example.project.data.model.Configuracao

class ConfigurationViewModel(private val apiService: ApiService) : ScreenModel {

    // O estado observável que guarda a configuração atual.
    var config = mutableStateOf<Configuracao?>(null)
        private set

    var isLoading = mutableStateOf(true)
        private set

    init {
        // Carrega a configuração da API assim que o ViewModel é criado.
        loadInitialConfig()
    }

    fun loadInitialConfig() {
        screenModelScope.launch {
            isLoading.value = true
            // Busca a primeira configuração disponível na API.
            val configs = apiService.getConfiguracoes()
            config.value = configs.firstOrNull()
            isLoading.value = false
        }
    }

    // Salva a configuração no backend e atualiza o estado local em caso de sucesso.
    suspend fun saveConfig(configToSave: Configuracao): Boolean {
        val savedId = apiService.saveConfiguracao(configToSave)
        return if (savedId != null) {
            // Atualiza o estado local com os dados salvos, incluindo o ID retornado.
            config.value = configToSave.copy(id = savedId)
            true
        } else {
            false
        }
    }
}
