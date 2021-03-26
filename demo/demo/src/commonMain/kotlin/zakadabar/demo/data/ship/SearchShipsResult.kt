/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.data.ship

import kotlinx.serialization.*
import zakadabar.stack.data.DtoBase

@Serializable
class SearchShipsResult(
    var shipId: Long,
    var name: String,
    var port: String?,
    var captain: String
) : DtoBase