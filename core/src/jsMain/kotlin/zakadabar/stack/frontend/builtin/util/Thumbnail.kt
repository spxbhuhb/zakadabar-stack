/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.data.BlobDto
import zakadabar.stack.frontend.elements.ZkElement

open class Thumbnail(val dto: BlobDto) : ZkElement() {
    override fun init() = build {
        + dto.name
    }
}