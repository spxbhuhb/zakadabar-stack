/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module.bundle

import zakadabar.cookbook.module.ExampleModule1
import zakadabar.cookbook.module.ExampleModule2
import zakadabar.stack.module.CommonModule
import zakadabar.stack.module.modules

class ExampleModuleBundle : CommonModule {

    override fun onModuleLoad() {
        modules += ExampleModule1()
        modules += ExampleModule2()
    }

}