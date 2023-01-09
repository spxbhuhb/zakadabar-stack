/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import zakadabar.core.server.Server

interface KtorConfigBuilder

class KtorAuthConfig(
    val build : (AuthenticationConfig.() -> Unit)
) : KtorConfigBuilder {

    fun runBuild(config : AuthenticationConfig) {
        config.build()
    }

}

class KtorRouteConfig(
    val build : (Route.() -> Unit)
) : KtorConfigBuilder {

    fun runBuild(config : Route) {
        config.build()
    }

}

class KtorPluginWithConfig<B : Any>(
    val feature : BaseApplicationPlugin<Application, B, *>,
    val config : (B.() -> Unit)?
)

operator fun Server.plusAssign(feature : BaseApplicationPlugin<Application, Any, *>) {
    features += KtorPluginWithConfig(feature) { }
}

operator fun Server.plusAssign(feature : KtorPluginWithConfig<*>) {
    features += feature
}

operator fun Server.plusAssign(configBuilder :KtorConfigBuilder) {
    configBuilders += configBuilder
}

operator fun <B : Any, F : BaseApplicationPlugin<Application, B, *>> F.invoke(config : B.() -> Unit) =
    KtorPluginWithConfig(this, config)