/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigator.messages

import zakadabar.stack.messaging.Message


data class PreviewEntityIntent(
    val entityId: Long?
) : Message