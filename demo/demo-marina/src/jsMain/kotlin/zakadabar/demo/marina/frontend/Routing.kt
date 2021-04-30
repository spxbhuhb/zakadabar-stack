/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.marina.frontend

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.demo.marina.frontend.pages.Home
import zakadabar.demo.marina.frontend.pages.port.Ports
import zakadabar.demo.marina.frontend.pages.sea.Seas
import zakadabar.demo.marina.frontend.pages.ship.ShipSearch
import zakadabar.demo.marina.frontend.pages.ship.Ships
import zakadabar.demo.marina.frontend.pages.speed.Speeds
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkNavState
import zakadabar.stack.frontend.builtin.pages.account.accounts.Accounts
import zakadabar.stack.frontend.builtin.pages.account.login.Login

/**
 * Extend AppRouting to define application components.
 *
 * Routing creates a [ZkNavState] from the URL and finds the
 * appropriate [ZkAppRouting.ZkTarget] to display to the user.
 *
 * Set the [ZkApplication.routing] field to this object
 * when the [ZkApplication] is created (typically in main.kt).
 *
 * You can change the routing of an application on-the-fly by
 * setting [ZkApplication.routing] to another object.
 */
class Routing : ZkAppRouting(DemoLayout, Home) {

    /**
     * Add ZkPage and ZkCrud implementations to the routing.
     * You can add anything else that implements [ZkTarget].
     *
     * Remember that when you do not specify the layout directly
     * [ZkAppRouting] uses the default layout passed as the first
     * parameter of the constructor.
     */
    init {
        + Home

        + Ships
        + ShipSearch
        + Speeds
        + Seas
        + Ports

        + Login
        + Accounts
        + Roles
    }

    /**
     * Override [onNavStateChange] to customize routing behaviour.
     * This example redirects the anonymous user to the login page.
     */
    override fun onNavStateChange(state: ZkNavState) {
        if (ZkApplication.executor.anonymous) {
            super.onNavStateChange(ZkNavState(Login.viewName, ""))
        } else {
            super.onNavStateChange(state)
        }
    }

}