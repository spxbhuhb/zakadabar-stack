/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.schema.BoSchema
import zakadabar.core.util.PublicApi

/**
 * Field context for standalone, readonly fields.
 */
object READONLY : ZkFieldContext {
    override val readOnly = true
    override val useShadow = false
    override val schema = BoSchema.NO_VALIDATION
    override val addLabel = true
    override val styles = zkFieldStyles
    override fun validate() {}
    override fun submit() {}
}

/**
 * Field context for standalone, writable fields.
 */
@PublicApi
object STANDALONE : ZkFieldContext {
    override val readOnly = false
    override val useShadow = false
    override val schema = BoSchema.NO_VALIDATION
    override val addLabel = true
    override val styles = zkFieldStyles
    override fun validate() {}
    override fun submit() {}
}

/**
 * Field context for standalone, writable fields without labels.
 */
@PublicApi
object STANDALONE_NOLABEL : ZkFieldContext {
    override val readOnly = false
    override val useShadow = false
    override val schema = BoSchema.NO_VALIDATION
    override val addLabel = false
    override val styles = zkFieldStyles
    override fun validate() {}
    override fun submit() {}
}

interface ZkFieldContext {

    val readOnly: Boolean

    val useShadow: Boolean

    val schema: BoSchema

    val addLabel: Boolean

    val styles: FieldStyleSpec

    fun validate()

    fun submit()

}