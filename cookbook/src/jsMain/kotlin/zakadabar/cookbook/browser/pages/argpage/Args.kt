/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.pages.argpage

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
data class Args(
    var a1: Int,
    var a2: Int
) : BaseBo {

    override fun schema() = BoSchema {
        + ::a1
        + ::a2
    }
}