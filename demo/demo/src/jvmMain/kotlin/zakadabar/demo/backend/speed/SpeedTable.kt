/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.speed

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.demo.data.speed.SpeedDto
import zakadabar.demo.data.speed.SpeedValues
import zakadabar.stack.backend.data.recordId

object SpeedTable : LongIdTable("speeds") {

    val description = varchar("description", 100)
    val value = enumeration("value", SpeedValues::class)

    fun toDto(row: ResultRow) = SpeedDto(
        id = row[id].recordId(),
        description = row[description],
        value = row[value]
    )

}