/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.marina.data.ship

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId

@Serializable
class SearchShipsResult(
    var shipId: RecordId<ShipDto>,
    var name: String,
    var port: String?,
    var captain: String
) : DtoBase