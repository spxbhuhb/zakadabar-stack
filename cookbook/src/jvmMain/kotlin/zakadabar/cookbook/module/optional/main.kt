/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module.optional

import zakadabar.stack.module.modules
import zakadabar.stack.module.optional

fun main(argv: Array<String>) {

    modules += ConsumerModule()

    modules.dependencies<ConsumerModule, ProviderModule1>().optional = true

    modules.resolveDependencies()
    modules.start()

    modules.first<ConsumerModule>().doSomething()
}