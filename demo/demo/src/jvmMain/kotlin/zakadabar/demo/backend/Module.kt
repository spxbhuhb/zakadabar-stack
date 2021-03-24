/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend

import zakadabar.demo.backend.account.AccountPrivateBackend
import zakadabar.demo.backend.account.AccountPublicBackend
import zakadabar.demo.backend.misc.PingBackend
import zakadabar.demo.backend.misc.WebsocketBackend
import zakadabar.demo.backend.port.PortBackend
import zakadabar.demo.backend.sea.SeaBackend
import zakadabar.demo.backend.ship.ShipBackend
import zakadabar.demo.backend.speed.SpeedBackend
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.role.RoleBackend
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantBackend
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {
    override fun onModuleLoad() {

        // these three are defined in the core project, you could write your own
        // but in most cases there are fine

        Server += RoleBackend
        Server += RoleGrantBackend
        Server += PrincipalBackend

        // you probably need something similar to these as most systems have
        // some account / session management

        Server += SessionBackend
        Server += AccountPrivateBackend
        Server += AccountPublicBackend

        // these are just for the demo

        Server += ShipBackend
        Server += SpeedBackend
        Server += PortBackend
        Server += SeaBackend

        Server += PingBackend
        Server += WebsocketBackend
    }
}