/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.plusAssign

object DefaultLayout : AppLayout("default") {

    override fun init() = build {
        initialized = true

        content.classList += coreClasses.layoutContent

        + row(coreClasses.h100) {
            + SideBar
            + content
        }
    }

}