/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.navigation.NavState
import zakadabar.stack.frontend.elements.ZkElement

object Layout : AppLayout("default") {

    private var activeElement: ZkElement? = null

    override fun init(): ZkElement {
        return this
    }

    override fun resume(state: NavState) {
        this -= activeElement
        activeElement = state.target.element(state)
        this += activeElement
        show()
    }

    override fun pause() {
        this -= activeElement
        activeElement = null
        hide()
    }

}