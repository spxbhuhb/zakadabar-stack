/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.backend

import zakadabar.samples.theplace.backend.ship.ShipBackend
import zakadabar.samples.theplace.backend.speed.SpeedBackend
import zakadabar.stack.backend.BackendContext
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {
    override fun init() {
        BackendContext += ShipBackend
        BackendContext += SpeedBackend
    }
}