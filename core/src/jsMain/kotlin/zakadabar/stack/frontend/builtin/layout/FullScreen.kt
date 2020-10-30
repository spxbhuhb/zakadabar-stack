/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.NavState
import zakadabar.stack.frontend.elements.ZkElement

/**
 * A very basic full screen layout that simply shows the target
 * element.
 */
object FullScreen : AppLayout("fullscreen") {

    private var activeElement: ZkElement? = null

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