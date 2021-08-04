/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module.serverless

import zakadabar.cookbook.module.ExampleModule1
import zakadabar.cookbook.module.ExampleModule2
import zakadabar.stack.module.modules

class Serverless {

    fun start() {
        modules += ExampleModule1()
        modules += ExampleModule2()

        modules.resolveDependencies()
        modules.start()
    }

    fun stop() {
        modules.stop()
    }
}