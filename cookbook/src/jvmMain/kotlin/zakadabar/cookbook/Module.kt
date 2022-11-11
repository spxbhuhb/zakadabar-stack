/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import zakadabar.cookbook.blob.attachment.AttachmentBlobBl
import zakadabar.cookbook.blob.crud.CrudBlobBl
import zakadabar.cookbook.blob.image.ImageBlobBl
import zakadabar.cookbook.entity.builtin.ExampleBl
import zakadabar.cookbook.entity.builtin.ExampleReferenceBl
import zakadabar.core.module.modules

fun install() {
    modules += ExampleReferenceBl()
    modules += ExampleBl()
    modules += AttachmentBlobBl()
    modules += ImageBlobBl()
    modules += CrudBlobBl()
}