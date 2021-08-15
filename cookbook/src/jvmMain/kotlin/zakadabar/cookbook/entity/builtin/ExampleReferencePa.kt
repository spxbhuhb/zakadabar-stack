/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.entity.builtin

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId

open class ExampleReferencePa : ExposedPaBase<ExampleReferenceBo,ExampleReferenceTable>(
    table = ExampleReferenceTable
) {
    fun count() = table.selectAll().count()

    override fun ResultRow.toBo() = ExampleReferenceBo(
        id = this[table.id].entityId(),
        name = this[table.name]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ExampleReferenceBo) {
        this[table.name] = bo.name
    }
}

object ExampleReferenceTable : ExposedPaTable<ExampleReferenceBo>(
    tableName = "example_reference"
) {

    internal val name = varchar("name", 50)

}