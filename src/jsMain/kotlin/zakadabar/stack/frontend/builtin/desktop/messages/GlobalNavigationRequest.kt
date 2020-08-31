/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.messaging.Message

/**
 * Sent when the user wants to navigate on application level. Application level
 * navigation means that the main content of the page will probably change.
 *
 * This is just a request does not mean that anything has been already changed.
 * When the navigation actually happens the code that performed the navigation
 * sends a [GlobalNavigationEvent] message.
 *
 * @property  location  The URL the user wants to navigate to.
 */
data class GlobalNavigationRequest(
    val location: String
) : Message {

    constructor(entityId: Long?, viewName: String? = null) : this(EntityRecordDto.viewUrl(entityId, viewName))

}