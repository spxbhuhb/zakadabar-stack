/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import kotlinx.browser.window
import zakadabar.stack.frontend.builtin.navigation.Navigation
import zakadabar.stack.frontend.builtin.navigation.NavigationState
import zakadabar.stack.frontend.builtin.navigation.navLink
import zakadabar.stack.frontend.elements.ComplexElement

class Home : ComplexElement() {

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
            NavigationState.StateType.Home -> TODO()

            NavigationState.StateType.Page -> {
                content.clearChildren()
                val pageState = state.pageState ?: return
                when (pageState.pageName) {
                    "Tortuga" -> content += Tortuga()
                    "Singapore" -> content += Singapore()
                }
            }

            NavigationState.StateType.View -> TODO()

            NavigationState.StateType.Unknown -> TODO()
        }

    }
}