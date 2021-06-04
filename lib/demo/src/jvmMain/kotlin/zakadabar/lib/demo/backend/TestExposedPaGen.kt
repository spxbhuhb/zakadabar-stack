/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.demo.data.TestBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for TestBo.
 * 
 * Generated with Bender at 2021-06-04T02:35:21.587Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class TestExposedPaGen : ExposedPaBase<TestBo,TestExposedTableGen>(
    table = TestExposedTableGen
) {
    override fun ResultRow.toBo() = TestBo(
        id = this[table.id].entityId(),
        name = this[table.name],
        value = this[table.value]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: TestBo) {
        this[table.name] = bo.name
        this[table.value] = bo.value
    }
}

/**
 * Exposed based SQL table for TestBo.
 * 
 * Generated with Bender at 2021-06-04T02:35:21.588Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object TestExposedTableGen : ExposedPaTable<TestBo>(
    tableName = "test"
) {

    internal val name = text("name")
    internal val value = integer("value")

}