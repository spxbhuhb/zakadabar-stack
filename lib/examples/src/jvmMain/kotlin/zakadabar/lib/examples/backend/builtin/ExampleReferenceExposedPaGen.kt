/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for ExampleReferenceBo.
 * 
 * Generated with Bender at 2021-05-30T14:06:04.723Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class ExampleReferenceExposedPaGen : ExposedPaBase<ExampleReferenceBo,ExampleReferenceExposedTableGen>(
    table = ExampleReferenceExposedTableGen
) {
    override fun ResultRow.toBo() = ExampleReferenceBo(
        id = this[table.id].entityId(),
        name = this[table.name]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ExampleReferenceBo) {
        this[table.name] = bo.name
    }
}

/**
 * Exposed based SQL table for ExampleReferenceBo.
 * 
 * Generated with Bender at 2021-05-30T14:06:04.724Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object ExampleReferenceExposedTableGen : ExposedPaTable<ExampleReferenceBo>(
    tableName = "example_reference"
) {

    internal val name = varchar("name", 50)

}