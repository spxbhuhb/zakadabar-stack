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