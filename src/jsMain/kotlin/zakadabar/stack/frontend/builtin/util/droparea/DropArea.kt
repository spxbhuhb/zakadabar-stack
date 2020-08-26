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

package zakadabar.stack.frontend.builtin.util.droparea

import org.w3c.dom.DataTransfer
import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.droparea.DropAreaClasses.Companion.classes
import zakadabar.stack.frontend.elements.ComplexElement

class DropArea(
    private val process: (DataTransfer) -> Unit,
    private val message: String = t("drop.files.here")
) : ComplexElement() {

    override fun init(): ComplexElement {

        this cssClass classes.dropArea build {
            + row(classes.dropAreaMessage) {
                + Icons.cloudUpload.simple18
                ! "&nbsp;"
                + message
            }
        }

        on("drop", ::onDrop)
        on("dragover", ::onDragOver)

        return this
    }

    private fun onDragOver(event: Event) {
        event.preventDefault()
    }

    private fun onDrop(event: Event) {
        event.stopPropagation()
        event.preventDefault()

        if (event !is DragEvent) return

        val dataTransfer = event.dataTransfer ?: return

        process(dataTransfer)
    }

}