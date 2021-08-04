/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid

open class JobPa(
    table: JobTable = JobTable()
) : ExposedPaBase<Job, JobTable>(
    table
) {

    override fun ResultRow.toBo() = Job(
        id = this[table.id].entityId(),
        status = this[table.status],
        createdAt = this[table.createdAt].toKotlinInstant(),
        completedAt = this[table.completedAt]?.toKotlinInstant(),
        startAt = this[table.startAt]?.toKotlinInstant(),
        specific = this[table.specific],
        actionNamespace = this[table.actionNamespace],
        actionType = this[table.actionType],
        actionData = this[table.actionData],
        node = this[table.node]?.toStackUuid(),
        failPolicy = this[table.failPolicy],
        failCount = this[table.failCount],
        failData = this[table.failData],
        deleteOnSuccess = this[table.deleteOnSuccess],
        progress = this[table.progress],
        responseData = this[table.responseData]
    )

    override fun UpdateBuilder<*>.fromBo(bo: Job) {
        this[table.status] = bo.status
        this[table.createdAt] = bo.createdAt.toJavaInstant()
        this[table.completedAt] = bo.completedAt?.toJavaInstant()
        this[table.startAt] = bo.startAt?.toJavaInstant()
        this[table.specific] = bo.specific
        this[table.actionNamespace] = bo.actionNamespace
        this[table.actionType] = bo.actionType
        this[table.actionData] = bo.actionData
        this[table.node] = bo.node?.toJavaUuid()
        this[table.failPolicy] = bo.failPolicy
        this[table.failCount] = bo.failCount
        this[table.failData] = bo.failData
        this[table.deleteOnSuccess] = bo.deleteOnSuccess
        this[table.progress] = bo.progress
        this[table.responseData] = bo.responseData
    }
}
