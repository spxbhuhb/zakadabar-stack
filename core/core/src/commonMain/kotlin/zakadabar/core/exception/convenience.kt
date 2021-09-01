/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.exception

import zakadabar.core.util.PublicApi
import kotlin.reflect.KProperty0

/**
 * Validate the value of the property and throw [BadRequest] when invalid. The exception
 * thrown contains one custom constraint fail for the property and with the name [customConstraintName].
 *
 * @param  block  The validation function to execute. When true, the property is valid, when false it is
 *                invalid.
 */
@PublicApi
inline fun <T> KProperty0<T>.validate(customConstraintName : String, block : (value : T) -> Boolean) {
    if (!block(this.get())) {
        throw BadRequest(this, customConstraintName)
    }
}