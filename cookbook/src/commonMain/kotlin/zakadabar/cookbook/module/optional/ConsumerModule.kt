/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module.optional

import zakadabar.stack.module.CommonModule
import zakadabar.stack.module.module
import zakadabar.stack.module.optionalModule

class ConsumerModule : CommonModule {

    // From the viewpoint of the module this is a mandatory dependency.
    // Functions using this dependency are OK to fail if someone sets
    // this dependency to optional.

    val provider1 by module<ProviderModule1>()

    // This is a truly optional dependency that may or may not be
    // present at the time the module runs. This has to be handled
    // properly each time the dependency is called.

    val provider2 by optionalModule<ProviderModule2>()

    fun doSomething() {

        println("")

        try {
            provider1.doSomething()
        } catch (ex : NullPointerException) {
            println("    calling module 1 is failed with a NullPointerException")
        }

        provider2?.doSomething() ?: println("    calling 2 didn't happen as there is no provider2 ")
    }
}