/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.table.Column
import zakadabar.demo.frontend.table.Table

class SpeedTable : Table<SpeedDto>() {

    val recordId by Column(SpeedDto::id)
    val description by Column(SpeedDto::description)
    val value by Column(SpeedDto::value)

}