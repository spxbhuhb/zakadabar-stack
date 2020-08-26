/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.backend.app

import kotlinx.serialization.Serializable

@Serializable
class Configuration(
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
