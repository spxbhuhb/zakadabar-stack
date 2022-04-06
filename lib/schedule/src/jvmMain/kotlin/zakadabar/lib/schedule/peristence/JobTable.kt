/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.peristence

import org.jetbrains.exposed.sql.`java-time`.timestamp
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.lib.schedule.data.Job
import zakadabar.lib.schedule.data.JobStatus

class JobTable : ExposedPaTable<Job>(
    tableName = "schedule_job"
) {

    val status = enumerationByName("status", 20, JobStatus::class)
    val createdBy = uuid("created_by")
    val createdAt = timestamp("created_at")
    val completedAt = timestamp("completed_at").nullable()
    val startAt = timestamp("start_at").nullable()
    val specific = bool("specific")
    val actionNamespace = varchar("action_namespace", 100)
    val actionType = varchar("action_type", 100)
    val actionData = text("action_data")
    val node = uuid("node").nullable()
    val failCount = integer("fail_count")
    val retryCount = integer("retry_count").default(0)
    val retryInterval = integer("retry_interval").default(0)
    val lastFailedAt = timestamp("last_failed_at").nullable()
    val lastFailMessage = text("last_fail_message").nullable()
    val lastFailData = text("last_fail_data").nullable()
    val deleteOnSuccess = bool("delete_on_success")
    val progress = double("progress")
    val progressText = text("progress_text").nullable()
    val lastProgressAt = timestamp("last_progress_at").nullable()
    val responseData = text("response_data").nullable()
    val origin = text("origin").nullable()

    init {
        index(isUnique = false, status, startAt)
    }
}