/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.blob.image

import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.util.io
import zakadabar.lib.blobs.browser.image.ZkImagesField

class ImagesField : ZkElement() {

    class Form : ZkForm<ExampleReferenceBo>() {
        override fun onCreate() {
            super.onCreate()

            + section {
                + ZkImagesField(
                    this@Form,
                    comm = ImageBlob.comm,
                    reference = bo.id,
                    blobCountMax = 2,
                    disposition = "image-blob-test",
                    blobClass = ImageBlob::class
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