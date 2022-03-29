/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.peristence

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import zakadabar.core.data.EntityId
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.util.UUID
import zakadabar.core.util.toJavaUuid
import zakadabar.core.util.toStackUuid
import zakadabar.lib.schedule.data.Job

open class JobPa(
    table: JobTable = JobTable()
) : ExposedPaBase<Job, JobTable>(
    table
) {

    override fun ResultRow.toBo() = Job(
        id = this[table.id].entityId(),
        status = this[table.status],
        createdBy = this[table.createdBy].toStackUuid(),
        createdAt = this[table.createdAt].toKotlinInstant(),
        completedAt = this[table.completedAt]?.toKotlinInstant(),
        startAt = this[table.startAt]?.toKotlinInstant(),
        specific = this[table.specific],
        actionNamespace = this[table.actionNamespace],
        actionType = this[table.actionType],
        actionData = this[table.actionData],
        worker = this[table.node]?.toStackUuid(),
        failCount = this[table.failCount],
        lastFailedAt = this[table.lastFailedAt]?.toKotlinInstant(),
        lastFailMessage = this[table.lastFailMessage],
        lastFailData = this[table.lastFailData],
        deleteOnSuccess = this[table.deleteOnSuccess],
        progress = this[table.progress],
        progressText = this[table.progressText],
        lastProgressAt = this[table.lastProgressAt]?.toKotlinInstant(),
        responseData = this[table.responseData],
        origin = this[table.origin]
    )

    override fun UpdateBuilder<*>.fromBo(bo: Job) {
        this[table.status] = bo.status
        this[table.createdBy] = bo.createdBy.toJavaUuid()
        this[table.createdAt] = bo.createdAt.toJavaInstant()
        this[table.completedAt] = bo.completedAt?.toJavaInstant()
        this[table.startAt] = bo.startAt?.toJavaInstant()
        this[table.specific] = bo.specific
        this[table.actionNamespace] = bo.actionNamespace
        this[table.actionType] = bo.actionType
        this[table.actionData] = bo.actionData
        this[table.node] = bo.worker?.toJavaUuid()
        this[table.failCount] = bo.failCount
        this[table.lastFailedAt] = bo.lastFailedAt?.toJavaInstant()
        this[table.lastFailMessage] = bo.lastFailMessage
        this[table.lastFailData] = bo.lastFailData
        this[table.deleteOnSuccess] = bo.deleteOnSuccess
        this[table.progress] = bo.progress
        this[table.progressText] = bo.progressText
        this[table.lastProgressAt] = bo.lastProgressAt?.toJavaInstant()
        this[table.responseData] = bo.responseData
        this[table.origin] = bo.origin
    }

    fun assignNode(jobId : EntityId<Job>, node : UUID) {
        table.update({ table.id eq jobId.toLong() }) {
            it[table.node] = node.toJavaUuid()
        }
    }
}
