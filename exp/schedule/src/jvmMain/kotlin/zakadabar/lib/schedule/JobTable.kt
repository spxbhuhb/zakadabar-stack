/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import org.jetbrains.exposed.sql.`java-time`.timestamp
import zakadabar.stack.backend.exposed.ExposedPaTable

class JobTable : ExposedPaTable<Job>(
    tableName = "schedule_job"
) {

    val status = enumerationByName("status", 20, JobStatus::class)
    val createdAt = timestamp("created_at")
    val completedAt = timestamp("completed_at").nullable()
    val startAt = timestamp("start_at").nullable()
    val specific = bool("specific")
    val actionNamespace = varchar("action_namespace", 100)
    val actionType = varchar("action_type", 100)
    val actionData = text("action_data")
    val node = uuid("node").nullable()
    val failCount = integer("fail_count")
    val lastFailData = text("fail_data").nullable()
    val deleteOnSuccess = bool("delete_on_success")
    val progress = double("progress")
    val progressText = text("progress_text").nullable()
    val lastProgressAt = timestamp("last_progress_at").nullable()
    val responseData = text("response_data").nullable()

    init {
        index(isUnique = false, status, startAt)
    }
}