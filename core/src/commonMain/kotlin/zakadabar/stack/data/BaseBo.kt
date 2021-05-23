/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import zakadabar.stack.data.schema.BoSchema

interface BaseBo {
    fun schema() = BoSchema.NO_VALIDATION

    val isValid
        get() = schema().validate().fails.isEmpty()
}