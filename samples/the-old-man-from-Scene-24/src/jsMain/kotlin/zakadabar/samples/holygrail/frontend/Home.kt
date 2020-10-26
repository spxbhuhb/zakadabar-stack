/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.holygrail.frontend

import kotlinx.browser.window
import zakadabar.samples.holygrail.data.rabbit.RabbitColors
import zakadabar.stack.frontend.application.navigation.Navigation
import zakadabar.stack.frontend.application.navigation.NavigationState
import zakadabar.stack.frontend.application.navigation.navLink
import zakadabar.stack.frontend.elements.ComplexElement

class Home : ComplexElement() {

    val content = ComplexElement()

    override fun init(): ComplexElement {
        this build {
            + col {
                + row {
                    + navLink(RabbitFrontend, Navigation.ALL, "Rabbits")
                    + navLink(RabbitFrontend, RabbitColors::class, "Colors")
                    + navLink(RabbitFrontend, Navigation.CREATE, "New Rabbit")
                }
                + content
            }
        }

        on(window, Navigation.EVENT, ::navigation)

        return this
    }

    private fun navigation() {
        val state = Navigation.state

        content.clearChildren()

        when (state.stateType) {
            NavState.StateType.Home -> return
            NavState.StateType.Page -> return
            NavState.StateType.Unknown -> return
            else -> Unit
        }

        val viewState = state.viewState ?: return

        when (viewState.viewName) {
            Navigation.CREATE -> content += RabbitFrontend.createView()
            Navigation.READ -> content += RabbitFrontend.readView()
            Navigation.ALL -> content += RabbitFrontend.allView()
            RabbitColors::class.simpleName -> RabbitFrontend.colorsView()
        }
    }
}