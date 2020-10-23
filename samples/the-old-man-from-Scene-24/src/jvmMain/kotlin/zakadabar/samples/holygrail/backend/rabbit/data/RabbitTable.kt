/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.holygrail.backend.rabbit.data

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import zakadabar.samples.holygrail.data.rabbit.RabbitDto
import zakadabar.stack.Stack

object RabbitTable : LongIdTable("t_${Stack.shid}_rabbits") {

    val name = varchar("name", 20)
    val color = varchar("color", 20)

    fun toDto(row: ResultRow) = RabbitDto(
        id = row[id].value,
        name = row[name],
        color = row[color]
    )

}