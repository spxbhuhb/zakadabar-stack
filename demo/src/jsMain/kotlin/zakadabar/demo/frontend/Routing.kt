/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.frontend

import zakadabar.demo.frontend.pages.misc.Home
import zakadabar.demo.frontend.pages.misc.Login
import zakadabar.demo.frontend.pages.port.Singapore
import zakadabar.demo.frontend.pages.port.Tortuga
import zakadabar.demo.frontend.pages.ship.Ships
import zakadabar.demo.frontend.pages.speed.Speeds
import zakadabar.stack.frontend.application.AppRouting
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.NavState

/**
 * Extend AppRouting to define application components.
 *
 * Routing creates a [NavState] from the URL and finds the
 * appropriate [AppRouting.ZkTarget] to display to the user.
 *
 * Set the [Application.routing] field to this object
 * when the [Application] is created (typically in main.kt).
 *
 * You can change the routing of an application on-the-fly by
 * setting [Application.routing] to another object.
 */
object Routing : AppRouting(DefaultLayout, Home) {

    /**
     * Add ZkPage and ZkCrud implementations to the routing.
     * You can add anything else that implements [ZkTarget].
     *
     * Remember that when you do not specify the layout directly
     * [AppRouting] uses the default layout passed as the first
     * parameter of the constructor.
     */
    init {
        + Ships
        + Speeds
        + Singapore
        + Tortuga
        + Login
    }

    /**
     * Override [onNavStateChange] to customize routing behaviour.
     * This example redirects the anonymous user to the login page.
     */
    override fun onNavStateChange(state: NavState) {
        if (! Application.executor.isAnonymous) {
            super.onNavStateChange(NavState(Login.viewName, ""))
        } else {
            super.onNavStateChange(state)
        }
    }

}