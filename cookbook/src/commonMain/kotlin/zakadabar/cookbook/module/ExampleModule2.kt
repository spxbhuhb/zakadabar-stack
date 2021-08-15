/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module

import zakadabar.core.module.CommonModule
import zakadabar.core.module.module

class ExampleModule2 : CommonModule {

    val m1 by module<ExampleModule1>()

    fun exampleFunction() {
        m1.world()
    }
}