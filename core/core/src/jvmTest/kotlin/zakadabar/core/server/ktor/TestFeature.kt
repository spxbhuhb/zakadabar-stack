/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.application.*
import io.ktor.util.*

class TestFeature(configuration: Configuration) {

    val value = configuration.value

    class Configuration {
        var value = "hello"
    }

    /**
     * Installable feature for [TestFeature].
     */
    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, TestFeature> {

        override val key = AttributeKey<TestFeature>("TestFeature")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): TestFeature {
            val configuration = Configuration().apply(configure)

            val feature = TestFeature(configuration)

            pipeline.intercept(ApplicationCallPipeline.Call) {  }

            return feature
        }
    }
}