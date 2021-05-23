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
package zakadabar.stack.frontend.builtin.form.fields

import kotlinx.browser.window
import org.w3c.dom.DragEvent
import org.w3c.dom.events.Event
import org.w3c.dom.get
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.BlobBo
import zakadabar.stack.data.entity.BlobCreateState
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.image.ZkImagePreview
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io

open class ZkImagesField<T : EntityBo<T>>(
    form: ZkForm<T>,
    private val dataEntityId: EntityId<T>,
    private val imageCountMax: Int? = null
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
                form.bo.comm().blobMetaList(dataEntityId).forEach {
                    + ZkImagePreview(it, onDelete = { preview -> onDelete(preview) }) marginRight 10 marginBottom 10
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

                    // this is a temporary bo to initialize the Thumbnail
                    val bo = BlobBo(EntityId(), null, "", file.name, file.type, file.size.toLong())

                    val thumbnail = ZkImagePreview(bo, BlobCreateState.Starting, onDelete = { preview -> onDelete(preview) })
                    thumbnail marginRight 10 marginBottom 10

                    // when the form is in create mode we don't have a proper record id, use null instead
                    form.bo.comm().blobCreate(
                        if (form.mode == ZkElementMode.Create) null else dataEntityId,
                        file.name, file.type, file,
                        thumbnail::update
                    )

                    // this will insert after the first thumbnail or at the beginning
                    insertAfter(thumbnail, childElements.filterIsInstance<ZkImagePreview>().lastOrNull())
                    updateDropArea()
                }
            }
        }
    }

    private fun allowUpload(): Boolean {
        return (imageCountMax == null || imageCountMax - childElements.count { it is ZkImagePreview } > 0)
    }

    private fun updateDropArea() {
        if (allowUpload()) {
            droparea.show()
        } else {
            droparea.hide()
        }
    }

    private suspend fun onDelete(preview: ZkImagePreview): Boolean {
        if (! ZkConfirmDialog(stringStore.confirmation.capitalize(), stringStore.confirmDelete).run()) return false

        if (form.mode != ZkElementMode.Create) {
            form.bo.comm().blobDelete(preview.bo.id)
        }

        this@ZkImagesField -= preview

        updateDropArea()

        return true
    }

    override fun onValidated(report: ValidityReport) {

    }

    override suspend fun onCreateSuccess(created: EntityBo<*>) {
        // update blobs with the proper record id
        childElements.filterIsInstance<ZkImagePreview>().forEach {
            @Suppress("UNCHECKED_CAST") // this is just an id, it should be fine
            form.bo.comm().blobMetaUpdate(it.bo.copy(entityId = created.id as EntityId<BaseBo>))
        }
    }


}