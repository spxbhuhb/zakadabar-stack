/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.holygrail.backend.rabbit.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.samples.holygrail.data.rabbit.RabbitDto

class RabbitDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RabbitDao>(RabbitTable)

    var name by RabbitTable.name
    var color by RabbitTable.color

    fun toDto() = RabbitDto(
        id = id.value,
        name = name,
        color = color
    )
}