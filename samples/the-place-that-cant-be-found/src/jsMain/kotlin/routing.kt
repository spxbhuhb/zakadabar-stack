/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.samples.theplace.frontend.Home
import zakadabar.samples.theplace.frontend.Ship
import zakadabar.samples.theplace.frontend.Singapore
import zakadabar.samples.theplace.frontend.Tortuga

@Suppress("unused") // this object provide optional access and is here for structural purposes
object Routing {

    val home by Home

    val singapore by Singapore
    val tortuga by Tortuga

    val ship by Ship

}
