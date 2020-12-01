/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend

import zakadabar.demo.backend.ship.ShipBackend
import zakadabar.demo.backend.speed.SpeedBackend
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {
    override fun init() {
        Server += ShipBackend
        Server += SpeedBackend
    }
}