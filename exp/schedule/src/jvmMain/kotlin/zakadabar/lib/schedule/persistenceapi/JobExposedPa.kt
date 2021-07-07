/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.persistenceapi

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.JobStatus
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid

open class JobExposedPa(
    table: JobExposedTable = JobExposedTable()
) : ExposedPaBase<Job, JobExposedTable>(
    table
) {

//    /**
//     * Poll a job from the table while using SQL update count as a lock mechanism.
//     * This is an optimistic approach that assumes that the lock is successful and
//     * pulls the action data before locking.
//     */
//    fun poll(action: Poll): List<PollResult> {
//        val result = table
//            .slice(table.id, table.actionNamespace, table.actionType, table.actionData)
//            .select { (table.status eq JobStatus.Pending) and (table.startAt lessEq Clock.System.now()) }
//            .orderBy(table.startAt)
//            .map {
//                PollResult(
//                    it[table.id].entityId(),
//                    it[table.actionNamespace],
//                    it[table.actionType],
//                    it[table.actionData]
//                )
//            }
//
//        return result.mapNotNull { entry ->
//            val updated = table.update({ table.id eq entry.jobId.toLong() and (table.status eq JobStatus.Pending) }) {
//                it[table.status] = JobStatus.Running
//                it[table.node] = action.node.toJavaUuid()
//            }
//            if (updated == 1) entry else null
//        }
//    }

    override fun ResultRow.toBo() = Job(
        id = this[table.id].entityId(),
        status = this[table.status],
        createdAt = this[table.createdAt].toKotlinInstant(),
        completedAt = this[table.completedAt]?.toKotlinInstant(),
        startAt = this[table.startAt]?.toKotlinInstant(),
        specificPoll = this[table.specificPoll],
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
        this[table.specificPoll] = bo.specificPoll
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

class JobExposedTable : ExposedPaTable<Job>(
    tableName = "job"
) {

    val status = enumerationByName("status", 20, JobStatus::class)
    val createdAt = timestamp("created_at")
    val completedAt = timestamp("completed_at").nullable()
    val startAt = timestamp("start_at").nullable()
    val specificPoll = bool("specific_poll")
    val actionNamespace = varchar("action_namespace", 100)
    val actionType = varchar("action_type", 100)
    val actionData = text("action_data")
    val node = uuid("node").nullable()
    val failPolicy = varchar("fail_policy", 100).nullable()
    val failCount = integer("fail_count")
    val failData = text("fail_data").nullable()
    val deleteOnSuccess = bool("delete_on_success")
    val progress = double("progress")
    val responseData = text("response_data").nullable()

    init {
        index(isUnique = false, status, startAt)
    }
}