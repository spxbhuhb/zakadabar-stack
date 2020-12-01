/*
 * Copyright © 2020, Simplexion, Hungary and contributors
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

import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.get
import zakadabar.stack.comm.http.BlobCreateState
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.BlobDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.Thumbnail
import zakadabar.stack.frontend.elements.ZkElement

class Images<T : RecordDto<T>>(
    val recordId: RecordId<T>,
    val comm: Comm<T>
) : ZkElement() {

    override fun init() = launchBuild {

        comm.blobMeta(recordId).forEach {
            + Thumbnail(it)
        }

        + div(DropAreaClasses.classes.dropArea) {
            + row(DropAreaClasses.classes.dropAreaMessage) {
                + Icons.cloudUpload.simple18
                ! "&nbsp;"
                + "drop files here"
            }
        }

        on("drop", ::onDrop)
        on("dragover", ::onDragOver)
    }

    private fun onDragOver(event: Event) {
        event.preventDefault()
    }

    private fun onDrop(event: Event) {
        event.stopPropagation()
        event.preventDefault()

        if (event !is DragEvent) return

        val dataTransfer = event.dataTransfer ?: return

        for (index in 0..dataTransfer.items.length) {

            val item = dataTransfer.items[index] ?: continue

            when (item.kind) {
                "file" -> {
                    val file = item.getAsFile() ?: continue
                    val dto = BlobDto(0, 0, "", file.name, file.type, file.size.toLong())
                    val thumbnail = Thumbnail(dto, BlobCreateState.Starting)
                    comm.blobCreate(recordId, file.name, file.type, file, thumbnail::update)
                    insertAfter(thumbnail, childElements.filterIsInstance<Thumbnail>().lastOrNull())
                }
            }
        }
    }

}