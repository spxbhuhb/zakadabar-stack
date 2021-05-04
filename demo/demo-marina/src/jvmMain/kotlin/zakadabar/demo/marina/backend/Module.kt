/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.backend

import zakadabar.demo.marina.backend.port.PortBackend
import zakadabar.demo.marina.backend.sea.SeaBackend
import zakadabar.demo.marina.backend.ship.ShipBackend
import zakadabar.demo.marina.backend.speed.SpeedBackend
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.account.AccountPrivateBackend
import zakadabar.stack.backend.data.builtin.account.AccountPublicBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.resources.SettingBackend
import zakadabar.stack.backend.data.builtin.resources.TranslationBackend
import zakadabar.stack.backend.data.builtin.role.RoleBackend
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantBackend
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {
    override fun onModuleLoad() {

        // these are defined in the core project, you could write your own
        // but in most cases there are fine

        Server += RoleBackend
        Server += RoleGrantBackend
        Server += PrincipalBackend
        Server += SessionBackend
        Server += TranslationBackend
        Server += SettingBackend

        // you probably need something similar to these as most systems have
        // some account / session management

        Server += AccountPrivateBackend
        Server += AccountPublicBackend

        // these are just for the demo

        Server += ShipBackend
        Server += SpeedBackend
        Server += PortBackend
        Server += SeaBackend
    }
}