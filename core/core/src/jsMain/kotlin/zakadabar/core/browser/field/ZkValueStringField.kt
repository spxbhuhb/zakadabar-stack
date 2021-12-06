/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

open class ZkValueStringField(
    context : ZkFieldContext,
    label: String,
    getter: () -> String,
    var setter: (String) -> Unit = { }
) : ZkStringBaseV2<String, ZkValueStringField>(
    context = context,
    getter =  getter,
    label = label
) {

    override var valueOrNull : String?
        get() = input.value
        set(value) {
            getter = { value }
            input.value = value!!
        }

    override fun setBackingValue(value: String) {
        setter(value)
        getter = { value }
        onUserChange(value)
    }

    override fun needsMandatoryMark() = stringMandatoryMark()
}