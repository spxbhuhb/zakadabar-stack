/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.application.*
import io.ktor.util.*

class TestFeature2(configuration: Configuration) {
    
    val value = configuration.value

    class Configuration {
        var value = "hello"
    }

    /**
     * Installable feature for [TestFeature2].
     */
    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, TestFeature2> {

        override val key = AttributeKey<TestFeature2>("TestFeature2")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): TestFeature2 {
            val configuration = Configuration().apply(configure)

            val feature = TestFeature2(configuration)

            pipeline.intercept(ApplicationCallPipeline.Call) {  }

            return feature
        }
    }
}