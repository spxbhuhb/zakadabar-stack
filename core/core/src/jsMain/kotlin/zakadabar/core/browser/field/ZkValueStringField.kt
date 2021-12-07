/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

open class ZkValueStringField(
    context : ZkFieldContext,
    label: String,
    getter: () -> String,
    setter: (String) -> Unit = { }
) : ZkStringBaseV2<String, ZkValueStringField>(
    context = context,
    getter =  getter,
    label = label,
    setter = setter
) {

    override var valueOrNull : String?
        get() = input.value
        set(value) {
            input.value = value!!
        }

    override fun setBackingValue(value: String) {
        setter(value)
        onUserChange(value)
    }

    override fun needsMandatoryMark() = stringMandatoryMark()
}