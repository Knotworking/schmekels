package com.knotworking.schmekels.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.knotworking.schmekels.core.logging.AppLog

object HttpClientFactory {
    fun create(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) = AppLog.d("Ktor", message)
            }
            level = LogLevel.BODY
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}
