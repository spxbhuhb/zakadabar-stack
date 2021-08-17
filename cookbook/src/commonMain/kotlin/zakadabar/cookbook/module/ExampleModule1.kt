/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module

import zakadabar.core.module.CommonModule

class ExampleModule1 : CommonModule {
    override fun onModuleStart() {
        println("Hello")
    }

    fun world() {
        println("World")
    }
}