/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.NavState
import zakadabar.stack.frontend.elements.ZkElement

object Layout : AppLayout("default") {

    private var initialized = false
    private var activeElement: ZkElement? = null
    private var content = ZkElement()

    override fun init(): ZkElement {

        build {
            + row {
                + Menu
                + content
            }
        }

        return this
    }

    override fun resume(state: NavState, target: ZkElement) {
        if (! initialized) init()

        this -= activeElement
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