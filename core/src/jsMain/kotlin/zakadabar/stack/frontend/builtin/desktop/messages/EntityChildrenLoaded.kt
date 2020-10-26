/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.messaging.Message

/**
 * Sent when the list of children of the given entity are loaded from the backend.
 */
data class EntityChildrenLoaded(
    val entityId: Long?,
    val children: List<EntityRecordDto>,
    val error: String?
) : Message