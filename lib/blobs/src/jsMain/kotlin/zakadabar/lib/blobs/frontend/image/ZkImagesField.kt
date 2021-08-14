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

import org.w3c.files.File
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCommInterface
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.lib.blobs.frontend.ZkBlobField
import zakadabar.lib.blobs.frontend.ZkBlobFieldEntry
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import kotlin.reflect.KClass

/**
 * Form field to handle images.
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
 * @param   showMeta         When true, metadata is shown, passed to [ZkImagePreview].
 * @param   blobClass        The class of blob instances. When no special initialization is needed,
 *                           the field can create the blobs by itself from this class.
 * @param   makeBlobCb       Callback function to make a blob instance.
 */
open class ZkImagesField<T : EntityBo<T>, BT : BlobBo<BT, T>>(
    form: ZkForm<T>,
    comm: BlobCommInterface<BT, T>,
    reference: EntityId<T>? = null,
    blobCountMax: Int? = null,
    disposition: String? = null,
    blobClass: KClass<BT>? = null,
    val showMeta : Boolean = false,
    makeBlobCb: ((File) -> BT)? = null
) : ZkBlobField<T, BT>(
    form, comm, reference, blobCountMax, disposition, blobClass, makeBlobCb
) {

    override fun onCreate() {
        super.onCreate()

        + zkLayoutStyles.row
    }

    override fun makeEntry(blob: BT, state : BlobCreateState?): ZkBlobFieldEntry<BT> {
        return ZkImagePreview(blob, createState = state, showMeta = showMeta, onDelete = { preview -> onDelete(preview) })
            .apply { this marginRight 10 marginBottom 10 }
    }

}