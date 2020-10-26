/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.messaging.Message

/**
 * Indicates that the given entity has been added.
 */
data class EntityAdded(
    val dto: EntityRecordDto
) : Message