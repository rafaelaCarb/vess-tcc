package org.example.project.data

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.example.project.data.model.Avaliacao
import org.example.project.data.model.Configuracao
import org.example.project.data.model.User

class ApiService {

    suspend fun getAvaliacoes(): List<Avaliacao> {
        return try {
            httpClient.get("$BASE_URL/avaliacao").body()
        } catch (e: Exception) {
            println("Erro ao buscar avaliações: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAvaliacaoById(id: Long): Avaliacao? {
        return try {
            httpClient.get("$BASE_URL/avaliacao/$id").body()
        } catch (e: Exception) {
            println("Erro ao buscar avaliação com ID $id: ${e.message}")
            null
        }
    }
    suspend fun getConfiguracoes(): List<Configuracao> {
        return try {
            httpClient.get("$BASE_URL/configuracao").body()
        } catch (e: Exception) {
            println("Erro ao buscar configurações: ${e.message}")
            emptyList()
        }
    }

    suspend fun saveConfiguracao(configuracao: Configuracao): Long? {
        return try {
            val response = httpClient.post("$BASE_URL/configuracao") {
                contentType(ContentType.Application.Json)
                setBody(configuracao)
            }
            response.body<Long>()
        } catch (e: Exception) {
            println("Erro ao salvar configuração: ${e.message}")
            null
        }
    }
    suspend fun saveAvaliacao(avaliacao: Avaliacao): Long? {
        return try {
            val response = httpClient.post("$BASE_URL/avaliacao") {
                contentType(ContentType.Application.Json)
                setBody(avaliacao)
            }
            response.body<Long>()
        } catch (e: Exception) {
            println("Erro ao salvar avaliação: ${e.message}")
            null
        }
    }
    suspend fun getUsers(): List<User> {
        return try {
            httpClient.get("$BASE_URL/users").body()
        } catch (e: Exception) {
            println("Erro ao buscar usuários: ${e.message}")
            emptyList()
        }
    }
}
