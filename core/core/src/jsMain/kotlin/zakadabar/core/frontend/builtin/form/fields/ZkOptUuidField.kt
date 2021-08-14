/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.frontend.builtin.form.fields

import zakadabar.core.util.UUID
import kotlin.reflect.KMutableProperty0

open class ZkOptUuidField(
    context : ZkFieldContext,
    prop: KMutableProperty0<UUID?>
) : ZkStringBase<UUID?>(
    context = context,
    prop = prop
) {

    companion object {
        val pattern = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}\$")
    }

    override fun getPropValue() = prop.get()?.toString() ?: ""

    override fun setPropValue(value: String) {

        if (value.isEmpty()) {
            invalidInput = false
            prop.set(null)
        }

        if (! value.matches(pattern)) {
            invalidInput = true
            return
        }

        invalidInput = false

        prop.set(UUID(value))
    }

}