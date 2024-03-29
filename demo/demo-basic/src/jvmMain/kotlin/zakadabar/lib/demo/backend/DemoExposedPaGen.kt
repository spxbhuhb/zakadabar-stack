/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.demo.data.DemoBo
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId
import zakadabar.lib.demo.enums.Test


/**
 * Exposed based Persistence API for DemoBo.
 * 
 * Generated with Bender at 2021-06-04T02:35:21.587Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class DemoExposedPaGen : ExposedPaBase<DemoBo,DemoExposedTableGen>(
    table = DemoExposedTableGen
) {
    override fun ResultRow.toBo() = DemoBo(
        id = this[table.id].entityId(),
        name = this[table.name],
        value = this[table.value],
        test = this[table.test]
    )  

    override fun UpdateBuilder<*>.fromBo(bo: DemoBo) {
        this[table.name] = bo.name
        this[table.value] = bo.value
        this[table.test] = bo.test
    }
}

/**
 * Exposed based SQL table for DemoBo.
 * 
 * Generated with Bender at 2021-06-04T02:35:21.588Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object DemoExposedTableGen : ExposedPaTable<DemoBo>(
    tableName = "demo"
) {

    internal val name = text("name")
    internal val value = integer("value")
    internal val test = enumerationByName("test", 20, Test::class).index().nullable()

}