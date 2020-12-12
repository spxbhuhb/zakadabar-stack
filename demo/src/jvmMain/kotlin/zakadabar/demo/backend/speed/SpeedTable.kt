/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.speed

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.data.SpeedDto

object SpeedTable : LongIdTable("speeds") {

    val description = varchar("description", 100)
    val value = double("value")

    fun toDto(row: ResultRow) = SpeedDto(
        id = row[id].value,
        description = row[description],
        value = row[value]
    )

}