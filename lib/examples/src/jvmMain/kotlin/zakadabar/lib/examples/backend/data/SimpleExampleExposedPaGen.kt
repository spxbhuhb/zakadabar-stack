/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.exposed.ExposedPaBase
import zakadabar.stack.backend.data.exposed.ExposedPaTable


/**
 * Exposed based Persistence API for SimpleExampleBo.
 * 
 * Generated with Bender at 2021-05-25T05:35:31.982Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class SimpleExampleExposedPaGen : ExposedPaBase<SimpleExampleBo>(
    table = SimpleExampleExposedTableGen()
)

/**
 * Exposed based SQL table for SimpleExampleBo.
 * 
 * Generated with Bender at 2021-05-25T05:35:31.982Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
class SimpleExampleExposedTableGen : ExposedPaTable<SimpleExampleBo>(
    tableName = "simple_example"
) {

    private val name = varchar("name", 30)

    override fun toBo(row: ResultRow) = SimpleExampleBo(
        id = row[id].entityId(),
        name = row[name]
    )

    override fun fromBo(statement: UpdateBuilder<*>, bo: SimpleExampleBo) {
        statement[name] = bo.name
    }

}