/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.samples.theplace.ThePlace
import zakadabar.samples.theplace.frontend.*
import zakadabar.stack.frontend.application.AppRouting

@Suppress("unused") // this object provide optional access and is here for structural purposes
object Routing : AppRouting(ThePlace.shid) {

    val home by target { Home }

    val singapore by target { Singapore }
    val tortuga by target { Tortuga }

    val ships by target { ships() }
    val createShip by target { createShip() }
    val readShip by target { readShip(it.recordId) }
    val updateShip by target { updateShip(it.recordId) }
    val deleteShip by target { deleteShip(it.recordId) }

}