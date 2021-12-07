/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field

import zakadabar.core.util.UUID
import kotlin.reflect.KMutableProperty0

open class ZkValueUuidField(
    context : ZkFieldContext,
    label: String,
    getter:() -> UUID,
    setter:(UUID) -> Unit = {}
) : ZkStringBaseV2<UUID,ZkValueUuidField>(
    context = context,
    label = label,
    getter = { getter().toString() },
    setter = setter
) {

    companion object {
        val pattern = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}\$")
    }

    override var valueOrNull : UUID?
        get() = input.value.ifEmpty { null }?.let { UUID(it) }
        set(value) {
            input.value = value.toString()
        }

    override fun setBackingValue(value: String) {
        if (value.isBlank() || ! value.matches(pattern)) {
            invalidInput = true
            context.validate()
            return
        }

        invalidInput = false

        val iv = UUID(value)
        setter(iv)
        onUserChange(iv)
    }

}