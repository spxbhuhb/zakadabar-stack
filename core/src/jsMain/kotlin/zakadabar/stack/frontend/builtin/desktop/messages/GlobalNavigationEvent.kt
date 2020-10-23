/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.messaging.Message

/**
 * Sent after a [GlobalNavigationRequest] has been processed and the application
 * navigates to another location.
 *
 * Components that display location dependent content have to update themselves
 * upon receiving a [GlobalNavigationEvent].
 *
 * @property  location  The URL the application navigates to.
 */
data class GlobalNavigationEvent(
    val location: String
) : Message