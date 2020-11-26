/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.droparea

import org.w3c.dom.DataTransfer
import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.files.File
import zakadabar.stack.comm.websocket.util.PushContent
import zakadabar.stack.data.BlobDto
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.droparea.DropAreaClasses.Companion.classes
import zakadabar.stack.frontend.comm.util.pushBlob
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch

open class DropArea(
    private val message: String = t("drop.files.here"),
    private val onProgress: (dto: BlobDto, state: PushContent.PushState, position: Long) -> Unit = { _, _, _ -> }
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
                "file" -> createFile(item.getAsFile() !!)
            }

            index ++
        }
    }

    private fun createFile(file: File) {
        launch { pushBlob(file.name, file.type, file, onProgress) }
    }

}