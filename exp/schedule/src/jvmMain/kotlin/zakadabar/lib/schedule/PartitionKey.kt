/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

/**
 * Key for a partition.
 */
open class PartitionKey(
    val actionNamespace: String?,
    val actionType: String?
)

fun Job.partitionKey() = PartitionKey(actionNamespace, actionType)

fun Subscription.partitionKey() = PartitionKey(actionNamespace, actionType)