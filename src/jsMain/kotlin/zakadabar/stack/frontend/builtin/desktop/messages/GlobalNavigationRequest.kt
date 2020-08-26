/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.frontend.builtin.desktop.messages

import zakadabar.stack.Stack
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
    constructor(entityId: Long?, viewName: String? = null) : this(
        when {
            entityId == null -> "/api/${Stack.shid}/entities"
            viewName != null -> "/api/${Stack.shid}/entities/$entityId/$viewName"
            else -> "/api/${Stack.shid}/entities/$entityId"
        }
    )
}