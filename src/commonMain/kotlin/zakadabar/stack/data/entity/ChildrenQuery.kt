/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

@Serializable
class ChildrenQuery(
    val parentId: Long?
) {
    suspend fun execute() = EntityRecordDto.comm.query(this, kotlinx.serialization.serializer(), ListSerializer(EntityRecordDto.serializer()))
}

