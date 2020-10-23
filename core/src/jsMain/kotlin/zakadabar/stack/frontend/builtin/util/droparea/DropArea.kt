/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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