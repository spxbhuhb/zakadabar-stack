/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import kotlinx.browser.document
import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.navigation.NavState
import zakadabar.stack.frontend.elements.ComplexElement

object FullScreen : AppLayout("fullscreen") {

    private var activeElement: ComplexElement? = null

    init {
        document.body?.appendChild(element)
        hide()
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