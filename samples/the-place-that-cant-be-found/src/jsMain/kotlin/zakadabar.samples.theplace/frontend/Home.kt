/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import kotlinx.browser.window
import zakadabar.stack.frontend.application.navigation.Navigation
import zakadabar.stack.frontend.application.navigation.NavigationState
import zakadabar.stack.frontend.application.navigation.navLink
import zakadabar.stack.frontend.elements.ComplexElement

object Home : ComplexElement(), Route {

    override val path = "/"

    val content = ComplexElement()

    override fun init(): ComplexElement {
        this build {
            + col {
                + row {
                    + navLink(Tortuga::class, "Tortuga")
                    + navLink(Singapore::class, "Singapore")
                }
                + content
            }
        }

        on(window, Navigation.EVENT, ::navigation)

        return this
    }

    private fun navigation() {
        val state = Navigation.state

        when (state.stateType) {
            NavState.StateType.Home -> TODO()

            NavState.StateType.Page -> {
                content.clearChildren()
                val pageState = state.pageState ?: return
                when (pageState.pageName) {
                    "Tortuga" -> content += Tortuga
                    "Singapore" -> content += Singapore
                }
            }

            NavState.StateType.View -> TODO()

            NavState.StateType.Unknown -> TODO()
        }

    }
}