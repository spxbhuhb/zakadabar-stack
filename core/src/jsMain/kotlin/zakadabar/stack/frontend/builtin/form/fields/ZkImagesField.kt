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
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.record.BlobCreateState
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormMode
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.image.ZkImagePreview
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io

open class ZkImagesField<T : RecordDto<T>>(
    form: ZkForm<T>,
    private val dataRecordId: RecordId<T>,
    private val imageCountMax: Int? = null
) : ZkFieldBase<T, Unit>(
    form = form,
    propName = ""
) {

    lateinit var droparea: ZkElement

    override fun onCreate() {
        io {
            element.style.display = "flex"
            element.style.flexDirection = "row"

            form.fields += this@ZkImagesField

            if (form.mode != ZkFormMode.Create) {

                form.dto.comm().blobMetaRead(dataRecordId).forEach {
                    + ZkImagePreview(it, onDelete = { preview -> onDelete(preview) }) marginRight 10 marginBottom 10
                }

                droparea = zke {

                    + div(ZkFormStyles.imageDropArea) {
                        + column(ZkFormStyles.imageDropAreaMessage) {
                            style {
                                alignItems = "center"
                            }
                            + ZkIconButton(ZkIcons.cloudUpload) marginBottom 10
                            + "drop files here"
                        }
                    }

                    on("drop", ::onDrop)
                    on("dragover", ::onDragOver)

                } marginRight 10 marginBottom 10

                + droparea
                updateDropArea()
            }
        }
    }

    private fun onDragOver(event: Event) {
        event.preventDefault()
    }

    private fun onDrop(event: Event) {
        event.stopPropagation()
        event.preventDefault()

        if (! allowUpload()) {
            window.alert(builtin.cannotAttachMoreImage)
            return
        }

        if (event !is DragEvent) return

        val dataTransfer = event.dataTransfer ?: return

        for (index in 0..dataTransfer.items.length) {

            val item = dataTransfer.items[index] ?: continue

            when (item.kind) {
                "file" -> {
                    val file = item.getAsFile() ?: continue

                    // this is a temporary dto to initialize the Thumbnail
                    val dto = BlobDto(0, 0, "", file.name, file.type, file.size.toLong())
                    val thumbnail = ZkImagePreview(dto, BlobCreateState.Starting, onDelete = { preview -> onDelete(preview) })

                    // when the form is in create mode we don't have a proper record id, use null instead
                    form.dto.comm().blobCreate(
                        if (form.mode == ZkFormMode.Create) null else dataRecordId,
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

    private fun onDelete(preview: ZkImagePreview): Boolean {
        if (! window.confirm(builtin.confirmDelete)) return false
        io {
            if (form.mode != ZkFormMode.Create) {
                form.dto.comm().blobDelete(dataRecordId, preview.dto.id)
            }
            this@ZkImagesField -= preview
            updateDropArea()
        }
        return true
    }

    override fun onValidated(report: ValidityReport) {

    }

    override suspend fun onCreateSuccess(created: RecordDto<*>) {
        // update blobs with the proper record id
        childElements.filterIsInstance<ZkImagePreview>().forEach {
            form.dto.comm().blobMetaUpdate(it.dto.copy(dataRecord = created.id))
        }
    }


}