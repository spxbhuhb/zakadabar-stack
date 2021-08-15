/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module.serverless

import zakadabar.cookbook.module.ExampleModule2
import zakadabar.core.module.modules

val serverless = Serverless()

fun main(args : Array<String>) {
    serverless.start()

    modules.first<ExampleModule2>().exampleFunction()

    serverless.stop()
}