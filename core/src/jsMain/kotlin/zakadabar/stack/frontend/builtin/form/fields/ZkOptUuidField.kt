/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.stack.data.BaseBo
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkStringBase
import zakadabar.stack.util.UUID
import kotlin.reflect.KMutableProperty0

open class ZkOptUuidField<T : BaseBo>(
    form: ZkForm<T>,
    prop: KMutableProperty0<UUID?>
) : ZkStringBase<T, UUID?>(
    form = form,
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