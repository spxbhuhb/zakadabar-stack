/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.server.application.*
import io.ktor.util.*

class TestPlugin(configuration: Configuration) {

    val value = configuration.value

    class Configuration {
        var value = "hello"
    }

    /**
     * Installable feature for [TestPlugin].
     */
    companion object Feature : BaseApplicationPlugin<ApplicationCallPipeline, Configuration, TestPlugin> {

        override val key = AttributeKey<TestPlugin>("TestPlugin")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): TestPlugin {
            val configuration = Configuration().apply(configure)

            val feature = TestPlugin(configuration)

            pipeline.intercept(ApplicationCallPipeline.Call) {  }

            return feature
        }
    }
}