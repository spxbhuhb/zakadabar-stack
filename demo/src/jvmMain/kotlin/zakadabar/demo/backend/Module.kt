/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend

import zakadabar.demo.backend.account.account.AccountBackend
import zakadabar.demo.backend.account.role.RoleBackend
import zakadabar.demo.backend.account.rolegrant.RoleGrantBackend
import zakadabar.demo.backend.account.session.SessionBackend
import zakadabar.demo.backend.misc.PingBackend
import zakadabar.demo.backend.misc.WebsocketBackend
import zakadabar.demo.backend.ship.ShipBackend
import zakadabar.demo.backend.speed.SpeedBackend
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {
    override fun init() {
        Server += SessionBackend
        Server += AccountBackend
        Server += RoleBackend
        Server += RoleGrantBackend

        Server += ShipBackend
        Server += SpeedBackend

        Server += PingBackend
        Server += WebsocketBackend
    }
}