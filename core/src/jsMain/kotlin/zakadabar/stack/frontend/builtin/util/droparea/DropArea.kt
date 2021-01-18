/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.droparea

import org.w3c.dom.DataTransfer
import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.get
import zakadabar.stack.comm.http.BlobCreateState
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.frontend.builtin.util.droparea.DropAreaClasses.Companion.classes
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Icons

open class DropArea<C : Any>(
    private val message: String = "drop.files.here",
    private val recordId: Long,
    private val comm: Comm<*>,
    private val context: C,
    private val onProgress: (context: C, dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
) : ZkElement() {

    override fun init(): ZkElement {

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

        onProcess(dataTransfer)
    }

    private fun onProcess(dataTransfer: DataTransfer) {

        var index = 0
        val end = dataTransfer.items.length

        while (index < end) {

            val item = dataTransfer.items[index] ?: continue

            when (item.kind) {
                "file" -> {
                    val file = item.getAsFile() ?: continue
                    //comm.createBlob(recordId, file.name, file.type, file, context, onProgress)
                }
            }

            index ++
        }
    }
}