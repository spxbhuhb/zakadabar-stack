/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.cookbook.sqlite.bundle.ExampleBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId

open class ExamplePa : ExposedPaBase<ExampleBo, ExampleTable>(
    table = ExampleTable
) {
    override fun ResultRow.toBo() = ExampleBo(
        id = this[table.id].entityId(),
        c1 = this[ExampleTable.c1],
        c2 = this[ExampleTable.c2]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: ExampleBo) {
        this[ExampleTable.c1] = bo.c1
        this[ExampleTable.c2] = bo.c2
    }
}

object ExampleTable : ExposedPaTable<ExampleBo>(
    tableName = "zklite_example"
) {

    internal val c1 = varchar("c1", 20)
    internal val c2 = integer("c2")

}