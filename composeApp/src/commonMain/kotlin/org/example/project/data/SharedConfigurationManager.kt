package org.example.project.data


import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.example.project.data.ApiService
import org.example.project.data.model.Configuracao

// Usamos um 'object' para criar um Singleton. Haverá apenas uma instância
// deste gestor em todo o aplicativo.
object SharedConfigurationManager {

    private val apiService = ApiService()
    private val scope = CoroutineScope(Dispatchers.IO)

    // O estado observável que guarda a configuração atual para todo o app.
    var config = mutableStateOf<Configuracao?>(null)
        private set

    var isLoading = mutableStateOf(true)
        private set

    // Esta função será chamada uma vez quando o app iniciar.
    fun loadInitialConfig() {
        scope.launch {
            isLoading.value = true
            val configs = apiService.getConfiguracoes()
            config.value = configs.firstOrNull()
            isLoading.value = false
        }
    }

    // A tela de configuração chamará esta função para salvar.
    suspend fun saveConfig(configToSave: Configuracao): Boolean {
        val savedId = apiService.saveConfiguracao(configToSave)
        return if (savedId != null) {
            // Atualiza o estado compartilhado com os dados salvos.
            config.value = configToSave.copy(id = savedId)
            true
        } else {
            false
        }
    }
}
