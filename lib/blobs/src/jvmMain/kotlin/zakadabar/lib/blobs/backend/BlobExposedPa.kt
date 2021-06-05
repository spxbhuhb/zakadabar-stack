/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.backend

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.update
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.data.entity.EntityId

abstract class BlobExposedPa<T : BlobBo<T>>(
    table: BlobExposedTable<T>
) : ExposedPaBase<T, BlobExposedTable<T>>(
    table
) {

    abstract fun newInstance(): T

    override fun ResultRow.toBo() = newInstance().also {
        it.id = EntityId(this[table.id].value)
        it.reference = this[table.reference]?.entityId()
        it.name = this[table.name]
        it.mimeType = this[table.mimeType]
        it.size = this[table.size]
    }

    override fun UpdateBuilder<*>.fromBo(bo: T) {
        this[table.reference] = bo.reference?.let { if (it.isEmpty()) null else EntityID(it.toLong(), table.referenceTable) }
        this[table.name] = bo.name
        this[table.mimeType] = bo.mimeType
        this[table.size] = bo.size
    }

    fun listByReference(id: EntityId<*>) =
        table
            .select { table.reference eq id.toLong() }
            .map { it.toBo() }

    fun writeContent(id: EntityId<T>, content: ByteArray) {
        table.update({ table.id eq id.toLong() }) { it[table.content] = ExposedBlob(content) }
    }

    fun readContent(id: EntityId<T>) =
        table
            .slice(table.content)
            .select { table.id eq id.toLong() }
            .mapNotNull { it[table.content]?.bytes }
            .first()

}


open class BlobExposedTable<T : BlobBo<T>>(
    tableName: String,
    internal val referenceTable: LongIdTable,
) : ExposedPaTable<T>(
    tableName
) {

    internal val reference = reference("reference", referenceTable).nullable()
    internal val name = varchar("name", 200)
    internal val mimeType = varchar("mime_type", 100)
    internal val size = long("size")
    internal val content = blob("content").nullable()

}