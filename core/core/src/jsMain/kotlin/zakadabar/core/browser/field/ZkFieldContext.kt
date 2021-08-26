/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.schema.BoSchema

object READONLY : ZkFieldContext {
    override val readOnly = true
    override val useShadow = false
    override val schema = BoSchema.NO_VALIDATION
    override val addLabel = true
    override val styles = zkFieldStyles
    override fun validate() {}
}

interface ZkFieldContext {

    val readOnly: Boolean

    val useShadow: Boolean

    val schema: BoSchema

    val addLabel: Boolean

    val styles: ZkFieldStyles

    fun validate()

}