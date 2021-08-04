/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import kotlinx.datetime.Instant
import zakadabar.stack.data.entity.EntityId

open class PartitionJobEntry(
    open val jobId: EntityId<Job>,
    open val startAt: Instant?,
    open val specific: Boolean,
    open val actionNamespace: String,
    open val actionType: String
)