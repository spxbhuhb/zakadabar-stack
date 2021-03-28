/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import zakadabar.stack.data.schema.DtoSchema

interface DtoBase {
    fun schema() = DtoSchema.NO_VALIDATION

    val isValid
        get() = schema().validate().fails.isEmpty()
}