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
package zakadabar.lib.blobs.browser

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.files.File
import org.w3c.files.get
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.field.ZkFieldBase
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.modal.ZkConfirmDialog
import zakadabar.core.browser.util.io
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.AlignItems
import zakadabar.core.resource.localizedStrings
import zakadabar.core.schema.ValidityReport
import zakadabar.core.text.capitalized
import zakadabar.core.util.newInstance
import zakadabar.lib.blobs.browser.image.ZkImagePreview
import zakadabar.lib.blobs.comm.BlobCommInterface
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState
import kotlin.reflect.KClass

/**
 * Form field to handle blobs.
 *
 * Ways to create the blob instances (in order of precedence):
 *
 * - override [makeBlob]
 * - pass [makeBlobCb]
 * - pass [blobClass]
 *
 * When none of the above is used, an [IllegalStateException] is thrown on
 * blob create attempts.
 *
 * @param   form             The form this field belongs to.
 * @param   reference        The entity to which these images belong.
 * @param   blobCountMax     Maximum number of images allowed.
 * @param   disposition      Disposition of images, saved to the `disposition` field of the image.
 * @param   blobClass        The class of blob instances. When no special initialization is needed,
 *                           the field can create the blobs by itself from this class.
 * @param   hideUpload       Function to decide if the upload area is hidden or nor. Default returns with "false".
 * @param   makeBlobCb       Callback function to make a blob instance.
 * @param   maxBlobSize
 */
abstract class ZkBlobField<T : EntityBo<T>, BT : BlobBo<BT, T>>(
    val form: ZkForm<T>,
    open val comm: BlobCommInterface<BT, T>,
    open val reference: EntityId<T>? = null,
    open val blobCountMax: Int? = null,
    open val disposition: String? = null,
    open val blobClass: KClass<BT>? = null,
    open val hideUpload: () -> Boolean = { false },
    open val makeBlobCb: ((File) -> BT)? = null
) : ZkFieldBase<Unit, ZkBlobField<T, BT>>(
    context = form,
    propName = ""
) {

    open lateinit var droparea: ZkElement
    open lateinit var inputField: HTMLInputElement

    override var readOnly = form.readOnly

    override fun onCreate() {
        io {
            form.fields += this@ZkBlobField

            if (form.mode != ZkElementMode.Create) {
                reference?.let {
                    comm.byReference(it, disposition).forEach { blob ->
                        + makeEntry(blob)
                    }
                }
            }

            if (! hideUpload()) {
                droparea = ZkElement(document.createElement("label") as HTMLLabelElement) build {

                    + (document.createElement("input") as HTMLInputElement).apply {
                        id = "${this@ZkBlobField.id}-input"
                        type = "file"
                        multiple = true
                        style.display = "none"

                        inputField = this

                        addEventListener("click", { value = "" })
                        addEventListener("change", ::onFileSelect)
                    }

                    (element as HTMLLabelElement).htmlFor = "${this@ZkBlobField.id}-input"

                    + div(blobStyles.imageDropArea) {
                        + column(blobStyles.imageDropAreaMessage) {
                            + AlignItems.center
                            + ZkButton(ZkIcons.cloudUpload, flavour = ZkFlavour.Custom) marginBottom 10
                            + div {
                                buildPoint.style.whiteSpace = "nowrap"
                                + localizedStrings.dropFilesHere
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
    }

    /**
     * Creates [ZkBlobFieldEntry] to display the blob to the user. Used from
     * [onCreate] to add existing entries and from [onDrop] to create a new
     * entry when the user drops a file on the field.
     *
     * @param   blob   The blob to create the entry for.
     * @param   state  The state of blob upload when this is a newly created blob.
     */
    abstract fun makeEntry(blob: BT, state: BlobCreateState? = null): ZkBlobFieldEntry<BT>

    /**
     * Creates a blob instance from a file dropped on the drop area. Default implementation:
     *
     * - calls [makeBlobCb] if not null
     * - calls [blobClass].newInstance if not null and sets fields
     * - throws [IllegalStateException] when both is null
     *
     * @param   file   JavaScript [File] to create the blob from.
     */
    open fun makeBlob(file: File): BT {
        makeBlobCb?.invoke(file)?.also { return it }

        blobClass?.newInstance()?.also {
            it.id = EntityId()
            it.reference = reference
            it.disposition = disposition ?: ""
            it.name = file.name
            it.mimeType = file.type
            it.size = file.size.toLong()
            return it
        }

        throw IllegalStateException("both blobClass and makeBlobCb is null")
    }

    open fun onDragOver(event: Event) {
        event.preventDefault()
    }

    open fun onDrop(event: Event) {
        event.stopPropagation()
        event.preventDefault()

        if (event !is DragEvent) return

        val dataTransfer = event.dataTransfer ?: return

        if (! allowUpload(dataTransfer.items.length)) {
            window.alert(localizedStrings.cannotAddMore)
            return
        }

        for (index in 0..dataTransfer.items.length) {
            dataTransfer.items[index]?.let { item ->
                if (item.kind == "file") item.getAsFile()?.upload()
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onFileSelect(event: Event) {
        inputField.files?.let { files ->

            if (! allowUpload(files.length)) {
                window.alert(localizedStrings.cannotAddMore)
                return
            }

            for (index in 0..files.length) {
                files[index]?.upload()
            }
        }
    }

    fun File.upload() {
        io {
            // this creates the blob BO on the server
            val bo = makeBlob(this).create()

            val entry = makeEntry(bo, BlobCreateState.Starting)

            // this will insert after the first thumbnail or at the beginning
            insertAfter(entry, find<ZkBlobFieldEntry<*>>().lastOrNull())
            updateDropArea()

            io { // this second IO block is here, so the upload will be launched in the background
                comm.upload(bo, this, callback = entry::update)
            }
        }
    }

    /**
     * Checks if the current state of the field allows upload. Do not
     * confuse with [hideUpload] that completely disables the upload
     * functionality.
     */
    open fun allowUpload(toUpload : Int = 0): Boolean {
        return blobCountMax?.let { max -> max - childElements.count { it is ZkBlobFieldEntry<*> } - toUpload >= 0 } ?: true
    }

    open fun updateDropArea() {
        if (hideUpload()) return
        if (allowUpload(1)) {
            droparea.show()
        } else {
            droparea.hide()
        }
    }

    open suspend fun onDelete(preview: ZkBlobFieldEntry<BT>): Boolean {
        if (! ZkConfirmDialog(localizedStrings.confirmation.capitalized(), localizedStrings.confirmDelete).run()) return false

        if (form.mode != ZkElementMode.Create) {
            comm.delete(preview.bo.id)
        }

        this@ZkBlobField -= preview

        updateDropArea()

        return true
    }

    override fun onValidated(report: ValidityReport) {

    }

    override suspend fun onCreateSuccess(created: EntityBo<*>) {
        // update blobs with the proper reference id
        find<ZkBlobFieldEntry<BT>>().forEach {
            @Suppress("UNCHECKED_CAST") // it is right
            it.bo.reference = created.id as EntityId<T>
            it.bo.update()
        }
    }

    /**
     * List blob BOs this field contains.
     */
    open fun blobs() = find<ZkImagePreview<BT>>().map { it.bo }

}