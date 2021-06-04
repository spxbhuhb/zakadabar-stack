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
package zakadabar.lib.blobs.frontend.image

import kotlinx.browser.window
import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.files.File
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCommInterface
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkFieldBase
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io

open class ZkImagesField<T : EntityBo<T>, BT : BlobBo<BT>>(
    form: ZkForm<T>,
    private val comm: BlobCommInterface<BT>,
    private val reference: EntityId<T>? = null,
    private val imageCountMax: Int? = null,
    private val make: (File) -> BT
) : ZkFieldBase<T, Unit>(
    form = form,
    propName = ""
) {

    open lateinit var droparea: ZkElement

    override fun onCreate() {
        io {
            element.style.display = "flex"
            element.style.flexDirection = "row"

            form.fields += this@ZkImagesField

            if (form.mode != ZkElementMode.Create) {
                reference?.let {
                    comm.listByReference(it).forEach {
                        + ZkImagePreview(it, onDelete = { preview -> onDelete(preview) }) marginRight 10 marginBottom 10
                    }
                }
            }

            droparea = zke {

                + div(ZkFormStyles.imageDropArea) {
                    + column(ZkFormStyles.imageDropAreaMessage) {
                        style {
                            alignItems = "center"
                        }
                        + ZkButton(ZkIcons.cloudUpload, flavour = ZkFlavour.Custom) marginBottom 10
                        + div {
                            buildPoint.style.whiteSpace = "nowrap"
                            + stringStore.dropFilesHere
                        }
                    }
                }

                on("drop", ::onDrop)
                on("dragover", ::onDragOver)

            } marginRight 10 marginBottom 10

            + droparea
            updateDropArea()
        }
    }

    private fun onDragOver(event: Event) {
        event.preventDefault()
    }

    private fun onDrop(event: Event) {
        event.stopPropagation()
        event.preventDefault()

        if (! allowUpload()) {
            window.alert(stringStore.cannotAttachMoreImage)
            return
        }

        if (event !is DragEvent) return

        val dataTransfer = event.dataTransfer ?: return

        for (index in 0..dataTransfer.items.length) {

            val item = dataTransfer.items[index] ?: continue

            when (item.kind) {
                "file" -> {
                    val file = item.getAsFile() ?: continue

                    // template::class(EntityId(), null, "", file.name, file.type, file.size.toLong())
                    io {
                        val bo = make(file).create()

                        val thumbnail = ZkImagePreview(bo, BlobCreateState.Starting, onDelete = { preview -> onDelete(preview) })
                        thumbnail marginRight 10 marginBottom 10

                        // this will insert after the first thumbnail or at the beginning
                        insertAfter(thumbnail, find<ZkImagePreview<*>>().lastOrNull())
                        updateDropArea()

                        io { // this second IO block is here, so the the upload will be launched in the background
                            comm.upload(bo, file, thumbnail::update)
                        }
                    }
                }
            }
        }
    }

    private fun allowUpload(): Boolean {
        return (imageCountMax == null || imageCountMax - childElements.count { it is ZkImagePreview<*> } > 0)
    }

    private fun updateDropArea() {
        if (allowUpload()) {
            droparea.show()
        } else {
            droparea.hide()
        }
    }

    private suspend fun onDelete(preview: ZkImagePreview<BT>): Boolean {
        if (! ZkConfirmDialog(stringStore.confirmation.capitalize(), stringStore.confirmDelete).run()) return false

        if (form.mode != ZkElementMode.Create) {
            comm.delete(preview.bo.id)
        }

        this@ZkImagesField -= preview

        updateDropArea()

        return true
    }

    override fun onValidated(report: ValidityReport) {

    }

}