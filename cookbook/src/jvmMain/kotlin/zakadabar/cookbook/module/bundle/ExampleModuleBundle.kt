/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.module.bundle

import zakadabar.cookbook.entity.builtin.ExampleBl
import zakadabar.cookbook.entity.builtin.ExampleReferenceBl
import zakadabar.stack.module.CommonModule
import zakadabar.stack.module.modules

class ExampleModuleBundle : CommonModule {

    override fun onModuleLoad() {

        modules += ExampleReferenceBl()
        modules += ExampleBl()

    }

}