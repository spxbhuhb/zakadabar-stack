/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.navigation.direction

import kotlinx.browser.sessionStorage
import kotlinx.browser.window
import org.w3c.dom.get
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.resource.css.px

open class NavigationDirection : ZkElement() {

    override fun onCreate() {
        + row {

            + buttonPrimary("Navigate to New") {
                with(application.routing) {
                    application.changeNavState(navState.urlPath, navState.urlQuery, navState.hash)
                }
            } marginRight 20.px

            + buttonPrimary("Go Forward") {
                application.forward()
            } marginRight 20.px

            + buttonPrimary("Go Back") {
                application.back()
            }
        } marginBottom 20.px

        val direction = when {
            application.routing.navState.backward -> "backward"
            application.routing.navState.forward -> "forward"
            application.routing.navState.new -> "new"
            else -> "unknown"
        }

        fun ZkElement.r(c: () -> Pair<String, String>) {
            val (f, s) = c()
            + tr {
                + td { + f }
                + td { + s }
            }
        }

        + table {
            r { "direction" to direction }
            r { "window.history.state" to window.history.state.toString() }
            r { "zk-nav-counter" to sessionStorage["zk-nav-counter"].toString() }
            r { "zk-nav-last-shown" to sessionStorage["zk-nav-last-shown"].toString() }
            r { "application.routing.navState" to application.routing.navState.toString() }
        }
    }

}