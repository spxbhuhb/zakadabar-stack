/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.data.ship

import kotlinx.serialization.Serializable

@Serializable
class SearchShipsResult(
    var id: Long,
    var name: String,
    var port: String,
    var captain: String
)