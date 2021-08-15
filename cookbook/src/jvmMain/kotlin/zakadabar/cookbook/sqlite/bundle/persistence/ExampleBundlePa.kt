/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import zakadabar.cookbook.sqlite.bundle.ExampleBundle
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId

open class ExampleBundlePa : ExposedPaBase<ExampleBundle, ExampleBundleTable>(
    table = ExampleBundleTable
) {
    override fun ResultRow.toBo() = ExampleBundle(
        id = this[table.id].entityId(),
        name = this[ExampleBundleTable.name]
    ).also {
        it.content = this[ExampleBundleTable.content].bytes
    }

    override fun UpdateBuilder<*>.fromBo(bo: ExampleBundle) {
        this[ExampleBundleTable.name] = bo.name
        this[ExampleBundleTable.content] = ExposedBlob(bo.content)
    }
}

object ExampleBundleTable : ExposedPaTable<ExampleBundle>(
    tableName = "example_bundle"
) {
    val name = varchar("name", 100)
    val content = blob("content")
}