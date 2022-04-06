/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId

@Serializable
class JobSummaryEntry(
    val jobId : EntityId<Job>,
    val status : JobStatus,
    val startAt : Instant?,
    val completedAt : Instant?,
    var actionNamespace : String,
    var actionType : String,
    var failCount: Int,
    var retryCount: Int,
    var lastFailedAt: Instant?
) : BaseBo