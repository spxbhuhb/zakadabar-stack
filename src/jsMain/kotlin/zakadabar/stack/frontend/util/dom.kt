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

package zakadabar.stack.frontend.util

import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event

/**
 * Look for an HTML element which has a DOM Node Id with the given prefix and is
 * an ancestor of the event target.
 *
 * @param  event  the browser event to get target from
 * @param  idPrefix  the id prefix to look for
 * @param  removePrefix  when true only part of the DOM Node Id after the prefix is returned
 *
 * @return  null if there is no element with the proper id between the ancestors
 *          a pair of the element and the id of the element (or part of id after the prefix if removePrefix is true)
 */
fun getElementId(event: Event, idPrefix: String, removePrefix: Boolean = true): Pair<HTMLElement, String>? {
    var current = event.target as Node?

    while (current != null) {
        if (current is HTMLElement && current.id.startsWith(idPrefix)) break
        current = current.parentNode
    }

    if (current == null) {
        return null
    }

    current as HTMLElement

    return if (removePrefix) {
        current to current.id.substring(idPrefix.length)
    } else {
        current to current.id
    }
}