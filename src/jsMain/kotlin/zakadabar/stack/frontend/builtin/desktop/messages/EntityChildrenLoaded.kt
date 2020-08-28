/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.messaging.Message

data class EntityChildrenLoaded(
    val entityId: Long?,
    val status: String? = null
) : Message