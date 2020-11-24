/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.stack.frontend.builtin.table.Table

@Suppress("unused") // table pattern
class SpeedTable : Table<SpeedDto>() {

    val recordId by column(SpeedDto::id)
    val description by column(SpeedDto::description)
    val value by column(SpeedDto::value)
    val update by updateLink(SpeedDto::id, Speeds)

}