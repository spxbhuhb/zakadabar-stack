/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.blob.attachment

import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.util.io
import zakadabar.lib.blobs.browser.attachment.ZkAttachmentsField

class AttachmentsField : ZkElement() {

    class Form : ZkForm<ExampleReferenceBo>() {
        override fun onCreate() {
            super.onCreate()

            + section {
                + ZkAttachmentsField(
                    this@Form,
                    comm = AttachmentBlob.comm,
                    reference = bo.id,
                    blobCountMax = 2,
                    disposition = "attachment-blob-test",
                    blobClass = AttachmentBlob::class
                )
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        io {
            + Form().also {
                it.bo = ExampleReferenceBo.all().first()
                it.mode = ZkElementMode.Update
            }
        }
    }

}