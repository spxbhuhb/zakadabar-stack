/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import zakadabar.core.server.Server

interface KtorConfigBuilder

class KtorAuthConfig(
    val build : (Authentication.Configuration.() -> Unit)
) : KtorConfigBuilder {

    fun runBuild(config : Authentication.Configuration) {
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

class KtorFeatureWithConfig<B : Any>(
    val feature : ApplicationFeature<Application, B, *>,
    val config : (B.() -> Unit)?
)

operator fun Server.plusAssign(feature : ApplicationFeature<Application, Any, *>) {
    features += KtorFeatureWithConfig(feature) { }
}

operator fun Server.plusAssign(feature : KtorFeatureWithConfig<*>) {
    features += feature
}

operator fun Server.plusAssign(configBuilder :KtorConfigBuilder) {
    configBuilders += configBuilder
}

operator fun <B : Any, F : ApplicationFeature<Application, B, *>> F.invoke(config : B.() -> Unit) =
    KtorFeatureWithConfig(this, config)