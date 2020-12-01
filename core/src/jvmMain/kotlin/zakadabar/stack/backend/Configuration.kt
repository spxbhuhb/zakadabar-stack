/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

import kotlinx.serialization.Serializable

@Serializable
class Configuration(
    val serverName: String = "Zakadabar Server",
    val database: DatabaseConfig,
    val systemApiKey: String? = null,
    val traceRouting: Boolean = false,
    val staticResources: String = "var/static",
    val ktor: KtorConfig = KtorConfig(),
    val modules: List<String> = emptyList()
)

@Serializable
data class DatabaseConfig(
    val driverClassName: String,
    val jdbcUrl: String,
    val username: String,
    val password: String
)

@Serializable
data class KtorConfig(
    val port: Int = 8080,
    val websocket: WebSocketConfig = WebSocketConfig()
)

@Serializable
data class WebSocketConfig(
    val pingPeriod: Long = 60L,
    val timeout: Long = 15L,
    val maxFrameSize: Long = 1000000L,
    val masking: Boolean = false
)
