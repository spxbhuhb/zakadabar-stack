/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.general

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses

object DefaultLayout : AppLayout("default") {


    override fun init() = build {
        initialized = true

        + row(coreClasses.h100) {
            + menu
            + content
        }
    }

}