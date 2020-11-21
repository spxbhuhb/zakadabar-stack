/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.demo.frontend.ThePlaceClasses.Companion.thePlaceClasses
import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.NavState
import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement

object Layout : AppLayout("default") {

    private var initialized = false
    private var activeElement: ZkElement? = null
    private var content = ZkElement()

    override fun init(): ZkElement {

        initialized = true

        with(element.style) {
            width = "100vw"
            height = "100vh"
        }

        build {
            + row(coreClasses.h100) {
                + Menu cssClass thePlaceClasses.menu
                + content
            }
        }

        return this
    }

    override fun resume(state: NavState, target: ZkElement) {
        if (! initialized) init()

        content -= activeElement
        activeElement = target
        content += activeElement

        show()
    }

    override fun pause() {
        content -= activeElement
        activeElement = null

        hide()
    }

}