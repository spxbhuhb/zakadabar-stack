/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.messaging.Message

/**
 * Indicates that the given entity has been changed.
 */
data class EntityUpdated(
    val entityDto: EntityDto
) : Message