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
package zakadabar.lib.blobs.frontend.attachment

import kotlinx.browser.window
import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.files.File
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCommInterface
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.lib.blobs.frontend.blobStyles
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkFieldBase
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io

open class ZkAttachmentsField<T : EntityBo<T>, BT : BlobBo<BT,T>>(
    form: ZkForm<T>,
    private val comm: BlobCommInterface<BT,T>,
    private val reference: EntityId<T>? = null,
    private val attachmentCountMax: Int? = null,
    private val disposition: String? = null,
    private val make: (File) -> BT
) : ZkFieldBase<T, Unit>(
    form = form,
    propName = ""
) {

    open lateinit var droparea: ZkElement

    override fun onCreate() {
        io {
            + blobStyles.attachmentField

            form.fields += this@ZkAttachmentsField

            if (form.mode != ZkElementMode.Create) {
                reference?.let {
                    comm.listByReference(it, disposition).forEach { blob ->
                        + ZkAttachmentEntry(blob, onDelete = { preview -> onDelete(preview) }) marginRight 10 marginBottom 10
                    }
                }
            }

            droparea = zke {

                + div(blobStyles.imageDropArea) {
                    + column(blobStyles.imageDropAreaMessage) {
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

                        val entry = ZkAttachmentEntry(bo, BlobCreateState.Starting, onDelete = { preview -> onDelete(preview) })
                        entry marginRight 10 marginBottom 10

                        // this will insert after the first thumbnail or at the beginning
                        insertAfter(entry, find<ZkAttachmentEntry<*>>().lastOrNull())
                        updateDropArea()

                        io { // this second IO block is here, so the the upload will be launched in the background
                            comm.upload(bo, file, entry::update)
                        }
                    }
                }
            }
        }
    }

    private fun allowUpload(): Boolean {
        return (attachmentCountMax == null || attachmentCountMax - childElements.count { it is ZkAttachmentEntry<*> } > 0)
    }

    private fun updateDropArea() {
        if (allowUpload()) {
            droparea.show()
        } else {
            droparea.hide()
        }
    }

    private suspend fun onDelete(preview: ZkAttachmentEntry<BT>): Boolean {
        if (! ZkConfirmDialog(stringStore.confirmation.capitalize(), stringStore.confirmDelete).run()) return false

        if (form.mode != ZkElementMode.Create) {
            comm.delete(preview.bo.id)
        }

        this@ZkAttachmentsField -= preview

        updateDropArea()

        return true
    }

    override fun onValidated(report: ValidityReport) {

    }

    override suspend fun onCreateSuccess(created: EntityBo<*>) {
        // update blobs with the proper reference id
        find<ZkAttachmentEntry<BT>>().forEach {
            @Suppress("UNCHECKED_CAST") // it is right
            it.bo.reference = created.id as EntityId<T>
            it.bo.update()
        }
    }


}